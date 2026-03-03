package andrey.service;

import andrey.dto.order.CreateNewOrderRequest;
import andrey.dto.order.OrderDto;
import andrey.dto.order.ProductReturnRequest;
import andrey.enums.OrderState;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<OrderDto> getUsersOrders(String userName);

    OrderDto createNewOrder(CreateNewOrderRequest request);

    OrderDto returnOrder(ProductReturnRequest returnRequest);

    OrderDto payOrderSuccess(UUID orderId);

    OrderDto changeOrderState(UUID orderId, OrderState state);

    OrderDto calculateTotalOrderPrice(UUID orderId);

    OrderDto calculateOrderDeliveryPrice(UUID orderId);
}
