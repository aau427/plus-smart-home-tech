package andrey.handlers.sensorevent;

import andrey.producer.KafkaClient;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

public class TemperatureSensorEventHandler extends AbstractSensorEventHandler<TemperatureSensorAvro> {
    public TemperatureSensorEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    protected TemperatureSensorAvro buildAvroPayLoad(SensorEventProto event) {
        final TemperatureSensorProto tmpEvent = event.getTemperatureSensorEvent();

        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(tmpEvent.getTemperatureC())
                .setTemperatureF(tmpEvent.getTemperatureC())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_EVENT;
    }
}
