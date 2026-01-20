package andrey.dto.sensor;

import andrey.dto.enums.SensorEventType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@Getter
@Setter
public class MotionSensorEvent extends SensorEvent {
    @NotNull(message = "Показатель качества связи обязателен")
    private Integer linkQuality;

    @NotNull(message = "Показатель наличия движения обязателен")
    private Boolean motion;

    @NotNull(message = "Показатель напряжения обязателен")
    private Integer voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
