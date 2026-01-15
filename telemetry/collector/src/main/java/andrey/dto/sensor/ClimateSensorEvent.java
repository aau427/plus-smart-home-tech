package andrey.dto.sensor;

import andrey.dto.enums.SensorEventType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@Getter
@Setter
public class ClimateSensorEvent extends SensorEvent {
    @NotNull(message = "Температура обязательна")
    private Integer temperatureC;

    @NotNull(message = "Влажность обязательна")
    private Integer humidity;

    @NotNull(message = "Уровень CO2 обязателен")
    private Integer co2Level;


    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
