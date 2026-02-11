package andrey.handlers.sensorevent;


import andrey.producer.KafkaClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Slf4j
public abstract class AbstractSensorEventHandler<T extends SpecificRecordBase> implements
        SensorEventHandler {

    protected final KafkaClient kafkaClient;
    @Value("${topic.sensors.event}")
    private String topic;

    @Autowired
    protected AbstractSensorEventHandler(final KafkaClient kafkaClient) {
        this.kafkaClient = kafkaClient;
    }

    protected abstract T buildAvroPayLoad(SensorEventProto event);

    @Override
    public void process(final SensorEventProto event) {
        log.info("Обрабатываю данные датчика: sensorId={}, hubId={}", event.getId(), event.getHubId());

        T payload = buildAvroPayLoad(event);
        log.debug("Создал payload: {}", payload.getClass().getName());

        Instant timestamp = Instant.ofEpochSecond(
                event.getTimestamp().getSeconds(),
                event.getTimestamp().getNanos());

        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setId(event.getId())
                .setTimestamp(timestamp)
                .setPayload(payload)
                .build();

        kafkaClient.sendEvent(topic, event.getHubId(), eventAvro, timestamp.toEpochMilli());

        log.info("Событие датчика отправлено в топик '{}'.", topic);
    }
}
