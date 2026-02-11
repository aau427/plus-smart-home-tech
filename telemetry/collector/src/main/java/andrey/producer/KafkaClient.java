package andrey.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaClient implements AutoCloseable {
    private final KafkaProducer<String, SpecificRecordBase> producer;

    @Override
    public void close() {
        log.info("Закрываю KafkaClient: сбрасываю буфер, останавливаю продюсер kafka, закрываю сетевые соединения");
        producer.flush();
        producer.close(Duration.ofSeconds(30));
        log.info("Kafka клиент успешно закрыт!");
    }

    public void sendEvent(String topic, String key, SpecificRecordBase record, long eventTimestamp) {
        ProducerRecord<String, SpecificRecordBase> producerRecord = new ProducerRecord<>(
                topic,
                null,            //партицию Kafka выберет сама
                eventTimestamp,  // 3. Тот самый TIMESTAMP события (в миллисекундах)
                key,
                record
        );


        producer.send(new ProducerRecord<>(topic, key, record), (recordMetadata, exception) -> {
            if (exception != null) {
                log.error("Ошибка при отправке события в топик {}: {}", topic, exception.getMessage(), exception);
            } else {
                log.info("Событие доставлено! Топик: {}; Партиция: {}; Оффсет: {}",
                        recordMetadata.topic(),
                        recordMetadata.partition(),
                        recordMetadata.offset());
            }
        });
    }
}
