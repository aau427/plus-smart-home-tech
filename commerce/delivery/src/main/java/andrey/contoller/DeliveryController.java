package andrey.contoller;

import andrey.dto.delivery.DeliveryDto;
import andrey.dto.order.OrderDto;
import andrey.service.DeliveryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public DeliveryDto createDelivery(@RequestBody @Valid DeliveryDto request) {
        return deliveryService.planDelivery(request);
    }

    //эмуляция успешной доставки товара
    @PostMapping("/successful")
    @ResponseStatus(HttpStatus.OK)
    public void confirmDelivery(@RequestBody @NotNull UUID orderId) {
        deliveryService.confirmDelivery(orderId);
    }

    //эмуляция получения товара в доставку
    @PostMapping("/picked")
    public void setDeliveryPicked(@RequestBody @NotNull UUID orderId) {
        deliveryService.setDeliveryPicked(orderId);
    }

    //эмуляция неудачной доставки (вручения товара)
    @PostMapping("/failed")
    public void setDeliveryFailed(@RequestBody @NotNull UUID orderId) {
        deliveryService.setDeliveryFailed(orderId);
    }

    //рассчет полной стоимости заказа
    @PostMapping("/cost")
    public BigDecimal calcTotalDeliveryPrice(@RequestBody @Valid OrderDto orderDto) {
        return deliveryService.calcTotalDeliveryPrice(orderDto);
    }
}
