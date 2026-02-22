package andrey.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookedProductsDto {

    //"Общий вес доставки", example = "15.5")
    @NotNull
    private BigDecimal deliveryWeight;

    //"Общий объём доставки", example = "0.45"
    @NotNull
    private BigDecimal deliveryVolume;

    //"Есть ли хрупкие вещи в доставке", example = "true")
    @NotNull
    private boolean fragile;
}
