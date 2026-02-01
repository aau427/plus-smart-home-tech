package andrey.handlers.sensorevent;

import andrey.producer.KafkaClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public class ClimateSensorEventHandler extends AbstractSensorEventHandler<ClimateSensorAvro> {

    public ClimateSensorEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT;
    }

    @Override
    protected ClimateSensorAvro buildAvroPayLoad(SensorEventProto event) {
        ClimateSensorProto tpmEvent = event.getClimateSensorEvent();

        return ClimateSensorAvro.newBuilder()
                .setCo2Level(tpmEvent.getCo2Level())
                .setHumidity(tpmEvent.getHumidity())
                .setTemperatureC(tpmEvent.getTemperatureC())
                .build();
    }
}

