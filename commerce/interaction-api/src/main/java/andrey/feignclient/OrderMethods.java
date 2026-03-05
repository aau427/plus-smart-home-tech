package andrey.feignclient;

import andrey.dto.order.CreateNewOrderRequest;
import andrey.dto.order.OrderDto;
import andrey.dto.order.ProductReturnRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderMethods {

    //регистрация заказа
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    OrderDto createNewOrder(@RequestBody @Valid CreateNewOrderRequest request);


    @PostMapping("/return")
    @ResponseStatus(HttpStatus.OK)
    OrderDto returnOrder(@RequestBody @Valid ProductReturnRequest returnRequest);

    //оплата заказа
    @PostMapping("/payment")
    @ResponseStatus(HttpStatus.OK)
    OrderDto payOrderSuccess(@RequestBody @NotNull UUID orderId);


    @PostMapping("/payment/failed")
    @ResponseStatus(HttpStatus.OK)
    OrderDto setPaymentFailed(@RequestBody @NotNull UUID orderId);

    @PostMapping("/delivery")
    @ResponseStatus(HttpStatus.OK)
    OrderDto delivery(@RequestBody @NotNull UUID orderId);

    @PostMapping("/delivery/failed")
    @ResponseStatus(HttpStatus.OK)
    OrderDto deliveryFailed(@RequestBody @NotNull UUID orderId);

    @PostMapping("/completed")
    @ResponseStatus(HttpStatus.OK)
    OrderDto setDeliveryComplete(@RequestBody @NotNull UUID orderId);

    //рассчет стоимости заказа
    @PostMapping("/calculate/total")
    @ResponseStatus(HttpStatus.OK)
    OrderDto calculateTotalOrderPrice(@RequestBody @NotNull UUID orderId);

    //рассчет стоимости доставки
    @PostMapping("/calculate/delivery")
    @ResponseStatus(HttpStatus.OK)
    OrderDto calculateOrderDeliveryPrice(@RequestBody @NotNull UUID orderId);

    //сборка заказа
    @PostMapping("/assembly")
    @ResponseStatus(HttpStatus.OK)
    OrderDto assembleOrder(@RequestBody @NotNull UUID orderId);
}
