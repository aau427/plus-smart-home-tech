package andrey.dto.warehouse;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AssemblyProductsForOrderRequest {

    @NotNull
    private UUID orderId;

    @NotNull
    @NotEmpty
    private Map<UUID, Long> products;
}