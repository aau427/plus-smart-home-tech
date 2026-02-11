package andrey.handlers.sensorevent;

import andrey.producer.KafkaClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class LightSensorEventHandler extends AbstractSensorEventHandler<LightSensorAvro> {

    public LightSensorEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    protected LightSensorAvro buildAvroPayLoad(SensorEventProto event) {
        final LightSensorProto tpmEvent = event.getLightSensorEvent();

        return LightSensorAvro.newBuilder()
                .setLinkQuality(tpmEvent.getLinkQuality())
                .setLuminosity(tpmEvent.getLuminosity())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }
}
