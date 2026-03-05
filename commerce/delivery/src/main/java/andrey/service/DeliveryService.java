package andrey.service;

import andrey.dto.delivery.DeliveryDto;
import andrey.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {
    DeliveryDto planDelivery(DeliveryDto request);

    void confirmDelivery(UUID orderId);

    void setDeliveryPicked(UUID orderId);

    void setDeliveryFailed(UUID orderId);

    BigDecimal calcTotalDeliveryPrice(OrderDto orderDto);
}
