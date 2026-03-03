package andrey.service;

import andrey.customutility.PaymentCalculator;
import andrey.dto.order.OrderDto;
import andrey.dto.payment.PaymentDto;
import andrey.enums.PaymentStatus;
import andrey.exception.payment.NotEnoughInfoInOrderToCalculateException;
import andrey.exception.payment.PaymentForOrderAlreadyExists;
import andrey.exception.payment.PaymentNotFoundException;
import andrey.feignclient.OrderMethods;
import andrey.mapper.PaymentMapper;
import andrey.model.Payment;
import andrey.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderMethods orderClient;
    private final PaymentCalculator paymentCalculator;

    @Transactional
    @Override
    public PaymentDto createOrderPayment(OrderDto orderDto) {
        if (paymentRepository.existsByOrderId(orderDto.getOrderId())) {
            throw new PaymentForOrderAlreadyExists(orderDto.getOrderId());
        }
        Payment newPayment = createPaymentFromOrderDto(orderDto);
        Payment savedPayment = paymentRepository.save(newPayment);
        log.debug("Зарегистрировал оплату {} по заказу {}",
                savedPayment.getPaymentId(),
                orderDto.getOrderId());
        return paymentMapper.toDto(savedPayment);
    }

    //ПОЛНАЯ СТОИМОСТЬ ЗАКАЗА
    @Override
    public BigDecimal calcOrderTotalCost(OrderDto orderDto) {
        if (
                orderDto.getProducts() == null ||
                        orderDto.getProducts().isEmpty() ||
                        orderDto.getDeliveryPrice() == null ||
                        orderDto.getDeliveryPrice().equals(BigDecimal.ZERO) ||
                        orderDto.getProductPrice() == null ||
                        orderDto.getProductPrice().equals(BigDecimal.ZERO)
        ) {
            throw new NotEnoughInfoInOrderToCalculateException(orderDto.getOrderId());
        }
        return paymentCalculator.calcTotalCost(orderDto);
    }

    //успешная оплата в платежном шлюзе
    @Transactional
    @Override
    public void confirmPayment(UUID paymentId) {
        Payment payment = getPaymentByIdOrThrowException(paymentId);
        payment.setStatus(PaymentStatus.SUCCESS);
        orderClient.payOrderSuccess(payment.getOrderId());
        paymentRepository.save(payment);
        log.debug("Оплата {} прошла успешно", paymentId);
    }

    //Расчёт стоимости товаров в заказе.
    @Override
    public BigDecimal calcProductCostInOrder(OrderDto orderDto) {
        if (orderDto.getProducts() == null || orderDto.getProducts().isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException(orderDto.getOrderId());
        }
        return paymentCalculator.calcProductsCost(orderDto);
    }

    //отказ в оплате
    @Transactional
    @Override
    public void cancelPayment(UUID paymentId) {
        Payment payment = getPaymentByIdOrThrowException(paymentId);
        payment.setStatus(PaymentStatus.FAILED);
        orderClient.setPaymentFailed(payment.getOrderId());
        paymentRepository.save(payment);
        log.debug("Отказ в оплате {}", paymentId);
    }

    private Payment createPaymentFromOrderDto(OrderDto orderDto) {
        BigDecimal fee = orderDto.getTotalPrice()
                .subtract(orderDto.getProductPrice())
                .subtract(orderDto.getDeliveryPrice());

        return Payment.builder()
                .paymentId(UUID.randomUUID())
                .orderId(orderDto.getOrderId())
                .status(PaymentStatus.PENDING)
                .deliveryTotal(orderDto.getDeliveryPrice())
                .totalPayment(orderDto.getTotalPrice())
                .feeTotal(fee)
                .build();
    }

    private Payment getPaymentByIdOrThrowException(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(
                        () -> new PaymentNotFoundException(paymentId)
                );
    }
}
