package andrey.dto.hub;

import andrey.dto.enums.DeviceType;
import andrey.dto.enums.HubEventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@Setter
@Getter
public class DeviceAddedEvent extends HubEvent {
    @NotBlank(message = "Идентификатор устройства не может быть пустым")
    private String id;

    @NotNull(message = "Тип устройства не может быть пустым")
    private DeviceType deviceType;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}
