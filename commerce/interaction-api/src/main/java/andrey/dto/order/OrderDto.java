package andrey.dto.order;

import andrey.enums.OrderState;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class OrderDto {
    @NotNull
    private UUID orderId;
    private UUID shoppingCartId;
    private Map<UUID, Long> products;
    private UUID paymentId;
    private UUID deliveryId;
    private OrderState state;
    private BigDecimal deliveryWeight;
    private BigDecimal deliveryVolume;
    private boolean fragile;
    private BigDecimal totalPrice;
    private BigDecimal deliveryPrice;
    private BigDecimal productPrice;
}
