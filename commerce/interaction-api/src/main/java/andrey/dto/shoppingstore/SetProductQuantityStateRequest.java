package andrey.dto.shoppingstore;

import andrey.enums.QuantityState;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SetProductQuantityStateRequest {

    @NotNull(message = "Не указан ID товара")
    private UUID productId;

    @NotNull(message = "Не указан статус товара")
    private QuantityState quantityState;
}
