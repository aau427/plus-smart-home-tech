package andrey.dto.sensor;

import andrey.dto.enums.SensorEventType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@Getter
@Setter
public class LightSensorEvent extends SensorEvent {
    @NotNull(message = "Качество связи (linkQuality) должно быть указано")
    private Integer linkQuality;

    @NotNull(message = "Уровень освещенности (luminosity) должен быть указан")
    private Integer luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
