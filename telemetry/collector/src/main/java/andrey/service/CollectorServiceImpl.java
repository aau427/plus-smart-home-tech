package andrey.service;

import andrey.dto.hub.HubEvent;
import andrey.dto.mapper.HubEventMapper;
import andrey.dto.mapper.SensorEventMapper;
import andrey.dto.sensor.SensorEvent;
import andrey.dto.kafka.KafkaClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        kafkaClient.sendEvent(topicForHubsEvent, hubEvent.getHubId(), hubEventMapper.toAvro(hubEvent));

    }

    @Override
    public void handleSensorEvent(SensorEvent sensorEvent) {
        log.info("Обрабатываю данные датчика: sensorId={}, hubId={}", sensorEvent.getId(), sensorEvent.getHubId());
        kafkaClient.sendEvent(topicForSensorsEvent, sensorEvent.getHubId(), sensorEventMapper.toAvro(sensorEvent));
    }
}
