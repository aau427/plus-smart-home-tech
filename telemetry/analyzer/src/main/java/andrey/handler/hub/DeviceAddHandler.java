package andrey.handler.hub;

import andrey.mapper.Mapper;
import andrey.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeviceAddHandler implements HubEventHandler {
    private final SensorRepository sensorRepository;
    private final Mapper mapper;

    @Override
    public String getType() {
        return DeviceAddedEventAvro.class.getSimpleName();
    }

    @Override
    @Transactional
    public void handle(HubEventAvro event) {
        sensorRepository.save(mapper.avroToSensor(event));
    }
}

