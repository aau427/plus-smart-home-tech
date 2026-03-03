package andrey.controller;

import andrey.dto.order.CreateNewOrderRequest;
import andrey.dto.order.OrderDto;
import andrey.dto.order.ProductReturnRequest;
import andrey.enums.OrderState;
import andrey.feignclient.OrderMethods;
import andrey.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController implements OrderMethods {

    private final OrderService orderService;

    //получить заказы пользователя
    @GetMapping
    List<OrderDto> getUsersOrdes(@RequestParam String userName) {
        return orderService.getUsersOrders(userName);
    }

    //Создать новый заказ в системе.
    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public OrderDto createNewOrder(@RequestBody @Valid CreateNewOrderRequest request) {
        return orderService.createNewOrder(request);
    }

    //возврат заказа
    @Override
    @PostMapping("/return")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto returnOrder(@RequestBody @Valid ProductReturnRequest returnRequest) {
        return orderService.returnOrder(returnRequest);
    }

    //оплата заказа
    @Override
    @PostMapping("/payment")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto payOrderSuccess(@RequestBody @NotNull UUID orderId) {
        return orderService.payOrderSuccess(orderId);
    }


    //Оплата заказа произошла с ошибкой.
    @Override
    @PostMapping("/payment/failed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto setPaymentFailed(@RequestBody @NotNull UUID orderId) {
        return orderService.changeOrderState(orderId, OrderState.PAYMENT_FAILED);
    }

    //Доставка заказа
    @Override
    @PostMapping("/delivery")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto delivery(@RequestBody @NotNull UUID orderId) {
        return orderService.changeOrderState(orderId, OrderState.DELIVERED);
    }

    //Доставка заказа произошла с ошибкой.
    @Override
    @PostMapping("/delivery/failed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto deliveryFailed(@RequestBody @NotNull UUID orderId) {
        return orderService.changeOrderState(orderId, OrderState.DELIVERY_FAILED);
    }

    //Завершение заказа
    @Override
    @PostMapping("/completed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto setDeliveryComplete(@RequestBody @NotNull UUID orderId) {
        return orderService.changeOrderState(orderId, OrderState.COMPLETED);
    }

    @Override
    //рассчет стоимости заказа
    @PostMapping("/calculate/total")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto calculateTotalOrderPrice(@RequestBody @NotNull UUID orderId) {
        return orderService.calculateTotalOrderPrice(orderId);
    }

    @Override
    //рассчет стоимости доставки
    @PostMapping("/calculate/delivery")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto calculateOrderDeliveryPrice(@RequestBody @NotNull UUID orderId) {
        return orderService.calculateOrderDeliveryPrice(orderId);
    }

    @Override
    //сборка заказа
    @PostMapping("/assembly")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto assembleOrder(@RequestBody @NotNull UUID orderId) {
        return orderService.changeOrderState(orderId, OrderState.ASSEMBLED);
    }
}
