package andrey.mapper;

import andrey.dto.order.CreateNewOrderRequest;
import andrey.dto.order.OrderDto;
import andrey.enums.OrderState;
import andrey.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = {UUID.class, OrderState.class})
public interface OrderMapper {

    @Mapping(target = "orderId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "shoppingCartId", source = "shoppingCart.shoppingCartId")
    @Mapping(target = "products", source = "shoppingCart.products")
    @Mapping(target = "state", expression = "java(OrderState.NEW)")
    // Поля, заполняемые после вызовов других микросервисов
    @Mapping(target = "deliveryId", ignore = true)
    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "deliveryVolume", ignore = true)
    @Mapping(target = "deliveryWeight", ignore = true)
    @Mapping(target = "fragile", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "productPrice", ignore = true)
    @Mapping(target = "deliveryPrice", ignore = true)
        // Если в ShoppingCartDto нет username, MapStruct оставит его null
    Order toEntity(CreateNewOrderRequest request);

    OrderDto toDto(Order order);
}
