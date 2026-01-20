package andrey.dto.mapper;

import andrey.dto.sensor.*;
import andrey.exception.UnknownEventException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Mapper(componentModel = "spring")
public abstract class SensorEventMapper {
    @Mapping(target = "payload", expression = "java(mapPayload(event))")
    public abstract SensorEventAvro toAvro(SensorEvent event);

    protected Object mapPayload(SensorEvent event) {
        if (event == null) return null;

        return switch (event.getType()) {
            case MOTION_SENSOR_EVENT -> toAvro((MotionSensorEvent) event);
            case TEMPERATURE_SENSOR_EVENT -> toAvro((TemperatureSensorEvent) event);
            case LIGHT_SENSOR_EVENT -> toAvro((LightSensorEvent) event);
            case CLIMATE_SENSOR_EVENT -> toAvro((ClimateSensorEvent) event);
            case SWITCH_SENSOR_EVENT -> toAvro((SwitchSensorEvent) event);
            case UNKNOWN_SENSOR_EVENT ->
                    throw new UnknownEventException("Тип события не поддерживается. Id хаба: " + event.getHubId());
        };
    }


    public abstract MotionSensorAvro toAvro(MotionSensorEvent event);

    public abstract TemperatureSensorAvro toAvro(TemperatureSensorEvent event);

    public abstract LightSensorAvro toAvro(LightSensorEvent event);

    public abstract ClimateSensorAvro toAvro(ClimateSensorEvent event);

    public abstract SwitchSensorAvro toAvro(SwitchSensorEvent event);

}
