package andrey.dto.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaClient implements AutoCloseable {
    private final KafkaProducer<String, SpecificRecordBase> kafkaProducer;

    @Override
    public void close() {
        log.info("Закрываю KafkaClient: сбрасываю буфер, останавливаю продюсер kafka, закрываю сетевые соединения");
        kafkaProducer.flush();
        kafkaProducer.close();
        log.info("Kafka клиент успешно закрыт!");
    }

    public void sendEvent(String topic, String key, SpecificRecordBase record) {
        kafkaProducer.send(new ProducerRecord<>(topic, key, record), (recordMetadata, exception) -> {
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
