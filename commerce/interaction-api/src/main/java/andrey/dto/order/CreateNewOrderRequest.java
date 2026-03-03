package andrey.dto.order;

import andrey.dto.shoppingcart.ShoppingCartDto;
import andrey.dto.warehouse.AddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreateNewOrderRequest {
    @NotNull
    @Valid
    private ShoppingCartDto shoppingCart;

    @NotNull
    String username;

    @NotNull
    @Valid
    private AddressDto deliveryAddress;
}
