package andrey.service;

import andrey.dto.delivery.DeliveryDto;
import andrey.dto.order.CreateNewOrderRequest;
import andrey.dto.order.OrderDto;
import andrey.dto.order.ProductReturnRequest;
import andrey.dto.payment.PaymentDto;
import andrey.dto.warehouse.AddressDto;
import andrey.dto.warehouse.AssemblyProductsForOrderRequest;
import andrey.dto.warehouse.BookedProductsDto;
import andrey.enums.OrderState;
import andrey.exception.order.NoOrderFoundException;
import andrey.feignclient.DeliveryMethods;
import andrey.feignclient.PaymentMethods;
import andrey.feignclient.WhSharedMethods;
import andrey.mapper.OrderMapper;
import andrey.model.Order;
import andrey.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final WhSharedMethods warehouseClient;
    private final DeliveryMethods deliveryClient;
    private final PaymentMethods paymentClient;

    @Override
    public List<OrderDto> getUsersOrders(String userName) {
        List<Order> orderList = orderRepository.findAllByUsername(userName);
        return orderList.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    //Создать новый заказ в системе.
    @Override
    @Transactional
    public OrderDto createNewOrder(CreateNewOrderRequest request) {
        Order order = orderMapper.toEntity(request);

        order = orderRepository.save(order);
        //Создаем BOOKING, DELIVERY и "дообогащаем" заказ.

        //собрать товары для заказа (по продуктовой корзине)
        BookedProductsDto bookingDto = createBooking(order);
        order.setFragile(bookingDto.isFragile());
        order.setDeliveryVolume(bookingDto.getDeliveryVolume());
        order.setDeliveryWeight(bookingDto.getDeliveryWeight());

        //создаем доставку
        DeliveryDto deliveryDto = createDelivery(order, request.getDeliveryAddress());
        order.setDeliveryId(deliveryDto.getDeliveryId());
        order.setDeliveryPrice(calcDeliveryPrice(order));

        order.setProductPrice(getProductsCost(order));
        order.setTotalPrice(getTotalPrice(order));

        //запускаем процесс оплаты
        PaymentDto paymentDto = createPayment(order);
        order.setPaymentId(paymentDto.getPaymentId());
        order.setState(OrderState.ON_PAYMENT);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Transactional
    @Override
    public OrderDto returnOrder(ProductReturnRequest returnRequest) {
        Order order = getOrderOrThrowException(returnRequest.getOrderId());
        warehouseClient.returnProducts(order.getProducts());
        order.setState(OrderState.PRODUCT_RETURNED);
        Order newOrder = orderRepository.save(order);
        return orderMapper.toDto(newOrder);
    }

    //оплата заказа прошла успешно....
    @Override
    public OrderDto payOrderSuccess(UUID orderId) {
        Order order = getOrderOrThrowException(orderId);
        order.setState(OrderState.PAID);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public OrderDto changeOrderState(UUID orderId, OrderState state) {
        Order order = getOrderOrThrowException(orderId);
        order.setState(state);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public OrderDto calculateTotalOrderPrice(UUID orderId) {
        Order order = getOrderOrThrowException(orderId);
        BigDecimal totalCost = paymentClient.calcOrderTotalCost(orderMapper.toDto(order));
        order.setTotalPrice(totalCost);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public OrderDto calculateOrderDeliveryPrice(UUID orderId) {
        Order order = getOrderOrThrowException(orderId);
        BigDecimal deliveryCost = deliveryClient.calcTotalDeliveryPrice(orderMapper.toDto(order));
        order.setDeliveryPrice(deliveryCost);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    private BookedProductsDto createBooking(final Order order) {
        AssemblyProductsForOrderRequest assemblyRequest = AssemblyProductsForOrderRequest.builder()
                .orderId(order.getOrderId())
                .products(order.getProducts())
                .build();
        return warehouseClient.assemblyProductsForOrder(assemblyRequest);
    }

    private DeliveryDto createDelivery(final Order order, final AddressDto toAddress) {
        DeliveryDto deliveryDto = DeliveryDto.builder()
                .orderId(order.getOrderId())
                .toAddress(toAddress)
                .fromAddress(warehouseClient.getWhAddress())
                .build();
        return deliveryClient.planDelivery(deliveryDto);
    }

    private BigDecimal calcDeliveryPrice(final Order order) {
        OrderDto orderDto = orderMapper.toDto(order);
        return deliveryClient.calcTotalDeliveryPrice(orderDto);
    }

    private PaymentDto createPayment(final Order order) {
        OrderDto orderDto = orderMapper.toDto(order);
        return paymentClient.createOrderPayment(orderDto);
    }

    private BigDecimal getProductsCost(final Order order) {
        OrderDto orderDto = orderMapper.toDto(order);
        return paymentClient.calcProductCostInOrder(orderDto);
    }

    private BigDecimal getTotalPrice(final Order order) {
        OrderDto orderDto = orderMapper.toDto(order);
        return paymentClient.calcOrderTotalCost(orderDto);
    }

    private Order getOrderOrThrowException(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new NoOrderFoundException(orderId)
                );
    }
}
