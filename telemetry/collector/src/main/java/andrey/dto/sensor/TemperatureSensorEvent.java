package andrey.dto.sensor;

import andrey.dto.enums.SensorEventType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@Getter
@Setter
public class TemperatureSensorEvent extends SensorEvent {
    @NotNull(message = "Температура в Цельсиях (temperatureC) обязательна")
    private Integer temperatureC;

    @NotNull(message = "Температура в Фаренгейтах (temperatureF) обязательна")
    private Integer temperatureF;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
