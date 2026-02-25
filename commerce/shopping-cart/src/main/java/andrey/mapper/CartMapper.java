package andrey.mapper;

import andrey.dto.shoppingcart.ShoppingCartDto;
import andrey.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartMapper {

    @Mapping(target = "shoppingCartId", source = "cartId")
    ShoppingCartDto toDto(ShoppingCart entity);
}
