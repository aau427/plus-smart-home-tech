package andrey.controller;

import andrey.dto.order.OrderDto;
import andrey.dto.payment.PaymentDto;
import andrey.feignclient.PaymentMethods;
import andrey.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController implements PaymentMethods {
    private final PaymentService paymentService;

    //Формирование оплаты для заказа
    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public PaymentDto createOrderPayment(@RequestBody @Valid OrderDto orderDto) {
        return paymentService.createOrderPayment(orderDto);
    }

    //Расчет полной стоимости заказа
    @Override
    @PostMapping("/totalCost")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal calcOrderTotalCost(@RequestBody @Valid OrderDto orderDto) {
        return paymentService.calcOrderTotalCost(orderDto);
    }

    //успешная оплата в платежной шлюзе
    @Override
    @PostMapping("/refund")
    @ResponseStatus(HttpStatus.OK)
    public void confirmPayment(@RequestBody UUID paymentId) {
        paymentService.confirmPayment(paymentId);
    }

    //Расчёт стоимости товаров в заказе.
    @Override
    @PostMapping("/productCost")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal calcProductCostInOrder(@RequestBody @Valid OrderDto orderDto) {
        return paymentService.calcProductCostInOrder(orderDto);
    }

    //отказ в оплате
    @Override
    @PostMapping("/failed")
    @ResponseStatus(HttpStatus.OK)
    public void cancelPayment(@RequestBody @NotNull UUID paymentId) {
        paymentService.cancelPayment(paymentId);
    }
}
