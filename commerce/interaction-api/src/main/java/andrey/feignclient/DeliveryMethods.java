package andrey.feignclient;

import andrey.dto.delivery.DeliveryDto;
import andrey.dto.order.OrderDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryMethods {
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    DeliveryDto planDelivery(@RequestBody @Valid DeliveryDto request);

    //эмуляция успешной доставки товара
    @PostMapping("/successful")
    @ResponseStatus(HttpStatus.OK)
    void confirmDelivery(@RequestBody @NotNull UUID orderId);

    //эмуляция получения товара в доставку
    @PostMapping("/picked")
    void setDeliveryPicked(@RequestBody @NotNull UUID orderId);

    //эмуляция неудачной доставки (вручения товара)
    @PostMapping("/failed")
    void setDeliveryFailed(@RequestBody @NotNull UUID orderId);

    //рассчет полной стоимости доставки заказа
    @PostMapping("/cost")
    BigDecimal calcTotalDeliveryPrice(@RequestBody @Valid OrderDto orderDto);
}
