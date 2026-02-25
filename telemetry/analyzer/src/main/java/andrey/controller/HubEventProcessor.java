package andrey.controller;

import andrey.factory.HubEventFactory;
import andrey.handler.hub.HubEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HubEventProcessor implements Runnable {
    private final KafkaConsumer<String, HubEventAvro> hubConsumer;
    private final HubEventFactory hubEventFactory;

    @Value("${kafka.topics.hub}")
    private String topic;

    @Override
    public void run() {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(hubConsumer::wakeup));
            hubConsumer.subscribe(List.of(topic));
            log.info("Analyzer: HubEventProcessor успешно подписался на топик {}", topic);
            while (true) {
                ConsumerRecords<String, HubEventAvro> records = hubConsumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    HubEventAvro event = record.value();
                    HubEventHandler handler = hubEventFactory.getHandlerForHubEvent(event.getPayload().getClass().getSimpleName())
                            .orElseThrow(() -> new IllegalArgumentException("Не найден обработчки для событя " + event.getSchema().getName()));
                    handler.handle(event);

                    TopicPartition partition = new TopicPartition(record.topic(), record.partition());
                    OffsetAndMetadata offsetMeta = new OffsetAndMetadata(record.offset() + 1);

                    hubConsumer.commitSync(Collections.singletonMap(partition, offsetMeta));
                }
            }
        } catch (WakeupException wakeupException) {
            log.error("Analyzer: поймайл WakeUp, закругляюсь!");
        } catch (Exception exception) {
            log.error("Analyzer: Поймал ошибку {}, закругляюсь!", exception.getMessage());
        } finally {
            hubConsumer.close(Duration.ofSeconds(1));
        }
    }
}
