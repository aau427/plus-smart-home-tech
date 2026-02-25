package andrey.dto.shoppingcart;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class ChangeProductQuantityRequest {

    @NotNull(message = "Не задан ID товара")
    private UUID productId;

    @NotNull(message = "Не задано количество")
    @Positive
    private Integer newQuantity;
}
