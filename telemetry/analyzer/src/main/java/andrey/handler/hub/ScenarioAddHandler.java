package andrey.handler.hub;

import andrey.model.Action;
import andrey.model.Condition;
import andrey.model.Scenario;
import andrey.repository.ActionRepository;
import andrey.repository.ConditionRepository;
import andrey.repository.ScenarioRepository;
import andrey.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScenarioAddHandler implements HubEventHandler {
    private final SensorRepository sensorRepository;
    private final ScenarioRepository scenarioRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;

    @Override
    public String getType() {
        return ScenarioAddedEventAvro.class.getSimpleName();
    }

    @Override
    @Transactional
    public void handle(HubEventAvro event) {
        if (!isAllSensorsExists(event)) {
            return;
        }
        ScenarioAddedEventAvro payload = (ScenarioAddedEventAvro) event.getPayload();
        String hubId = event.getHubId();
        Scenario scenarioEntity = scenarioRepository.findByHubIdAndName(hubId, payload.getName())
                .map(existing -> {
                    conditionRepository.deleteAllByScenarioId(existing.getId());
                    actionRepository.deleteAllByScenarioId(existing.getId());
                    conditionRepository.flush();
                    return existing;
                })
                .orElseGet(() -> scenarioRepository.save(
                        Scenario.builder()
                                .hubId(hubId)
                                .name(payload.getName())
                                .build()
                ));

        List<Condition> conditions = payload.getConditions().stream()
                .map(c -> Condition.builder()
                        .scenario(scenarioEntity)
                        .sensor(sensorRepository.getReferenceById(c.getSensorId()))
                        .type(c.getType())
                        .operation(c.getOperation())
                        .value(getAvroValue(c.getValue()))
                        .build())
                .toList();
        conditionRepository.saveAll(conditions);

        List<Action> actions = payload.getActions().stream()
                .map(a -> Action.builder()
                        .scenario(scenarioEntity)
                        .sensor(sensorRepository.getReferenceById(a.getSensorId()))
                        .type(a.getType())
                        .value(a.getValue())
                        .build())
                .toList();
        //я тихо схожу с ума....
        actionRepository.saveAll(actions);
        log.info("Сценарий '{}' для хаба {} успешно сохранен. Условий: {}, Действий: {}",
                payload.getName(), hubId, conditions.size(), actions.size());
    }

    private boolean isAllSensorsExists(HubEventAvro event) {
        ScenarioAddedEventAvro payload = (ScenarioAddedEventAvro) event.getPayload();
        Set<String> requiredSensorIds = Stream.concat(
                payload.getConditions().stream().map(ScenarioConditionAvro::getSensorId),
                payload.getActions().stream().map(DeviceActionAvro::getSensorId)
        ).collect(Collectors.toSet());
        long countOfExistingSensors = sensorRepository.countByIdInAndHubId(requiredSensorIds, event.getHubId());
        if (countOfExistingSensors < requiredSensorIds.size()) {
            log.error("Ошибка при сохранении сценария '{}' : найдено только {} из {} датчиков",
                    payload.getName(), countOfExistingSensors, requiredSensorIds.size());
            return false;
        } else {
            return true;
        }
    }

    private Integer getAvroValue(Object value) {
        if (value instanceof Integer i) return i;
        if (value instanceof Boolean b) return b ? 1 : 0;
        return null;
    }
}
