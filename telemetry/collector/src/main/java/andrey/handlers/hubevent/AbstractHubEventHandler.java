package andrey.handlers.hubevent;

import andrey.producer.KafkaClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@Slf4j
public abstract class AbstractHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {

    protected final KafkaClient kafkaClient;
    @Value("${topic.hubs.event}")
    private String topic;

    @Autowired
    public AbstractHubEventHandler(final KafkaClient kafkaClient) {
        this.kafkaClient = kafkaClient;
    }

    protected abstract T buildAvroPayLoad(HubEventProto event);

    @Override
    public void process(HubEventProto event) {

        log.info("Обрабатываю событие хаба: hubId={}, type={}", event.getHubId(), event.getPayloadCase());

        T payload = buildAvroPayLoad(event);

        Instant timestamp = Instant.ofEpochSecond(
                event.getTimestamp().getSeconds(),
                event.getTimestamp().getNanos());

        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(timestamp)
                .setPayload(payload)
                .build();

        kafkaClient.sendEvent(topic, event.getHubId(), eventAvro, timestamp.toEpochMilli());

        log.info("Событие хаба {} типа {} отправлено в топик '{}'.", eventAvro.getHubId(), event.getPayloadCase(), topic);
    }
}
