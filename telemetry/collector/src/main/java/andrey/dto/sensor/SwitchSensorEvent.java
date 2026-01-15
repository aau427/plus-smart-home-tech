package andrey.dto.sensor;

import andrey.dto.enums.SensorEventType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString(callSuper = true)
@Getter
@Setter
public class SwitchSensorEvent extends SensorEvent {
    @NotNull(message = "Состояние переключателя (state) должно быть указано")
    private Boolean state;

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
