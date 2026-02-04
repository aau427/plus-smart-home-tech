package andrey.handler.hub;

import andrey.repository.ActionRepository;
import andrey.repository.ConditionRepository;
import andrey.repository.ScenarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioRemovedHandler implements HubEventHandler {
    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;
    private final ScenarioRepository scenarioRepository;

    @Override
    public String getType() {
        return "ScenarioRemovedEventAvro";
    }

    @Override
    @Transactional // Удаляем всё дерево данных атомарно
    public void handle(HubEventAvro event) {
        ScenarioRemovedEventAvro payload = (ScenarioRemovedEventAvro) event.getPayload();
        String hubId = event.getHubId();
        String name = payload.getName();

        scenarioRepository.findByHubIdAndName(hubId, name).ifPresentOrElse(
                scenario -> {
                    conditionRepository.deleteAllByScenarioId(scenario.getId());
                    actionRepository.deleteAllByScenarioId(scenario.getId());
                    scenarioRepository.delete(scenario);
                    log.info("Сценарий '{}' хаба {} успешно удален", name, hubId);
                },
                () -> log.info("Удаление отклонено: сценарий '{}' хаба {} не найден", name, hubId)
        );
    }
}
