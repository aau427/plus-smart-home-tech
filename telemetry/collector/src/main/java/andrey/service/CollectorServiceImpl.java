package andrey.service;

import andrey.dto.hub.HubEvent;
import andrey.dto.hub.UnknownHubEvent;
import andrey.dto.kafka.KafkaClient;
import andrey.dto.mapper.HubEventMapper;
import andrey.dto.mapper.SensorEventMapper;
import andrey.dto.sensor.SensorEvent;
import andrey.dto.sensor.UnknownSensorEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollectorServiceImpl implements CollectorService {
    private final KafkaClient kafkaClient;
    private final SensorEventMapper sensorEventMapper;
    private final HubEventMapper hubEventMapper;

    @Value("${topic.sensors.event}")
    private String topicForSensorsEvent;

    @Value("${topic.hubs.event}")
    private String topicForHubsEvent;


    @Override
    public void handleHubEvent(HubEvent hubEvent) {
        log.info("Обрабатываю событие хаба: hubId={}, type={}", hubEvent.getHubId(), hubEvent.getType());
        if (hubEvent instanceof UnknownHubEvent) {
            log.warn("Попытка отправить неизвестное событие (UnknownHubEvent) для хаба {}. Не хочу отправлять и не буду.",
                    hubEvent.getHubId());
            return;
        }
        SpecificRecordBase specificRecordBase = hubEventMapper.toAvro(hubEvent);
        long eventTimeStamp = hubEvent.getTimestamp().toEpochMilli();
        kafkaClient.sendEvent(topicForHubsEvent, hubEvent.getHubId(), specificRecordBase, eventTimeStamp);

    }

    @Override
    public void handleSensorEvent(SensorEvent sensorEvent) {
        log.info("Обрабатываю данные датчика: sensorId={}, hubId={}", sensorEvent.getId(), sensorEvent.getHubId());
        if (sensorEvent instanceof UnknownSensorEvent) {
            log.warn("Попытка отправить неизвестное событие (UnknownSensorEvent) для хаба {}. Не хочу отправлять и не буду.",
                    sensorEvent.getHubId());
            return;
        }
        SpecificRecordBase specificRecordBase = sensorEventMapper.toAvro(sensorEvent);
        long eventTimeStamp = sensorEvent.getTimestamp().toEpochMilli();

        kafkaClient.sendEvent(topicForSensorsEvent, sensorEvent.getHubId(), specificRecordBase, eventTimeStamp);
    }
}
