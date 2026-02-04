package andrey.handler.hub;

import andrey.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeviceRemovedHandler implements HubEventHandler {
    private final SensorRepository sensorRepository;

    @Override
    public String getType() {
        return "DeviceRemovedEventAvro";
    }

    @Override
    public void handle(HubEventAvro event) {
        DeviceRemovedEventAvro deviceRemovedAvro = (DeviceRemovedEventAvro) event.getPayload();
        long countDelRecord = sensorRepository.deleteByIdAndHubId(deviceRemovedAvro.getId(), event.getHubId());
        if (countDelRecord == 0) {
            log.info("Попытка удалить несуществущий датчик {} хаба {}",
                    deviceRemovedAvro.getId(), event.getHubId());
        } else {
            log.info("Датчик {} хаба {} успешно удален",
                    deviceRemovedAvro.getId(), event.getHubId());
        }
    }
}
