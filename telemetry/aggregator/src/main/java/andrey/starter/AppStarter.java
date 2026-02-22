package andrey.starter;

import andrey.service.AggregatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppStarter {
    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final KafkaProducer<String, SensorsSnapshotAvro> producer;
    private final AggregatorService service;

    @Value("${aggregator.topics.consumer}")
    private String subscriptionTopic;

    @Value("${aggregator.topics.producer}")
    String topicForProducer;

    public void start() {
        /* Регистрируем хук, в котором при завершении приложения
        будет вызван метод wakeup.
        Это приведёт к генерации WakeupException в методе poll.
        После этого работа консьюмера завершится. */
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
        try {
            consumer.subscribe(List.of(subscriptionTopic));
            log.info("Агрегатор: Успешно подписались на топик {}", subscriptionTopic);
            while (true) {
                ConsumerRecords<String, SensorEventAvro> records =
                        consumer.poll(Duration.ofMillis(1000));
                log.debug("Агрегатор: получено {} сообщений", records.count());
                if (!records.isEmpty()) {
                    for (ConsumerRecord<String, SensorEventAvro> record : records) {
                        log.info("Агрегатор: Получено сообщение {}", record.value().getId());
                        Optional<SensorsSnapshotAvro> optEvent = service.updateState(record.value());
                        optEvent.ifPresent(this::sendSnapshot);
                    }
                    consumer.commitSync();
                }
            }
        } catch (WakeupException wakeupException) {
            log.warn("Поймали WakeupException, будет закругляться!");
        } catch (Exception e) {
            log.error("Ошибка при обработке события, будем закругляться!");
        } finally {
            consumer.close();
            producer.flush();
            producer.close(Duration.ofSeconds(30));
        }
    }

    private void sendSnapshot(SensorsSnapshotAvro snapshotAvro) {
        log.debug("Агрегатор: отправляю снепшот для хаба {} в топик {}",
                snapshotAvro.getHubId(),
                topicForProducer);
        log.debug("Агрегатор: текст снэпшота {}", snapshotAvro);
        final ProducerRecord<String, SensorsSnapshotAvro> record =
                new ProducerRecord<>(topicForProducer, snapshotAvro.getHubId(), snapshotAvro);
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                log.error("Failed to send snapshot to Kafka", exception);
            } else {
                log.info("Snapshot sent successfully to the topic {} at offset {}", metadata.topic(),
                        metadata.offset());
            }
        });
    }
}
