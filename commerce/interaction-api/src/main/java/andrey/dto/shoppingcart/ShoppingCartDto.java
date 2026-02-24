package andrey.dto.shoppingcart;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class ShoppingCartDto {

    @NotNull(message = "Не задан Id корзины")
    private UUID shoppingCartId;


    @NotNull(message = "Не задано ни одного идентификатора товара и его количество")
    private Map<UUID, Long> products;
}