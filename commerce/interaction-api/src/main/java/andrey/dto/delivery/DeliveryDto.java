package andrey.dto.delivery;

import andrey.dto.warehouse.AddressDto;
import andrey.enums.DeliveryState;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class DeliveryDto {
    private UUID deliveryId;

    @NotNull(message = "Не указан адрес отправителя")
    @Valid
    private AddressDto fromAddress;

    @NotNull(message = "Не указан адрес доставки")
    @Valid
    private AddressDto toAddress;

    @NotNull(message = "Не указан Id заказа")
    private UUID orderId;

    @NotNull(message = "Не указан статус доставки")
    private DeliveryState deliveryState;
}
