package andrey.controller;

import andrey.handler.snapshot.SnapshotEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SnapshotProcessor {
    private final KafkaConsumer<String, SensorsSnapshotAvro> snapshotConsumer;
    private final SnapshotEventHandler snapshotEventHandler;

    @Value("${kafka.topics.snapshot}")
    private String topic;

    public void start() {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(snapshotConsumer::wakeup));

            snapshotConsumer.subscribe(List.of(topic));
            log.info("Analyzer: подписался на топик: {}", topic);

            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = snapshotConsumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    snapshotEventHandler.handle(record.value());
                }

                if (!records.isEmpty()) {
                    snapshotConsumer.commitSync();
                }
            }
        } catch (WakeupException e) {
            log.info("Analyzer: SnapshotProcessor (main thread) останавливается...");
        } catch (Exception e) {
            log.error("Analyzer: Критическая ошибка в SnapshotProcessor: {}", e.getMessage());
        } finally {
            snapshotConsumer.close(Duration.ofSeconds(1));
        }
    }
}

