package andrey.handlers.sensorevent;

import andrey.producer.KafkaClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
public class MotionSensorEventHandler extends AbstractSensorEventHandler<MotionSensorAvro> {

    public MotionSensorEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    protected MotionSensorAvro buildAvroPayLoad(SensorEventProto event) {
        final MotionSensorProto tmpEvent = event.getMotionSensorEvent();


        return MotionSensorAvro.newBuilder()
                .setLinkQuality(tmpEvent.getLinkQuality())
                .setMotion(tmpEvent.getMotion())
                .setVoltage(tmpEvent.getVoltage())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }
}
