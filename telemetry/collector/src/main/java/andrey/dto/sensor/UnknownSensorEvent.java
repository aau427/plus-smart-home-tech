package andrey.dto.sensor;

import andrey.dto.enums.SensorEventType;

public class UnknownSensorEvent extends SensorEvent {
    @Override
    public SensorEventType getType() {
        return SensorEventType.UNKNOWN_SENSOR_EVENT;
    }
}
