package andrey.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductReturnRequest {
    @NotNull(message = "Номер заказа должен быть указан!")
    private UUID orderId;

    @NotNull(message = "Не указаны продукты в заказе")
    Map<UUID, Long> products;
}
