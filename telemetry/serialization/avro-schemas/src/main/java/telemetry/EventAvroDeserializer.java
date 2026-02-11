package telemetry;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public class EventAvroDeserializer extends BaseAvroDeserializer<SensorEventAvro> {
    public EventAvroDeserializer() {
        super(SensorEventAvro.getClassSchema());
    }
}
