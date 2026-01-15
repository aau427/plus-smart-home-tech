package andrey.dto.hub;

import andrey.dto.enums.ActionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeviceAction {
    @NotBlank(message = "Идентификатор датчика (устройства) не может быть пустым")
    private String sensorId;

    @NotNull(message = "Тип действия не может быть пустым")
    private ActionType type;

    //значение value опционально, например для inverse оно не указывается
    private Integer value;
}
