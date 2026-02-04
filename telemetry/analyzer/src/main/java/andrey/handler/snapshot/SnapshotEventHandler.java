package andrey.handler.snapshot;


import andrey.model.Condition;
import andrey.repository.ActionRepository;
import andrey.repository.ConditionRepository;
import andrey.repository.ScenarioRepository;
import andrey.transport.HubRouterClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;

@Component
@Slf4j
@RequiredArgsConstructor
public class SnapshotEventHandler {
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;
    private final ScenarioRepository scenarioRepository;
    private final HubRouterClient hubRouterClient;

    // словаь сравнений. вместо switch, каждому типу сравнения сопоставил функцию
    private final Map<ConditionOperationAvro, BiPredicate<Integer, Integer>> operations = Map.of(
            ConditionOperationAvro.EQUALS, Integer::equals,
            ConditionOperationAvro.GREATER_THAN, (current, target) -> current > target,
            ConditionOperationAvro.LOWER_THAN, (current, target) -> current < target
    );

    //правила - как брать значение для конкретного типа датчика. Господи, нафига я пошел учиться... я точно сдвинусь
    //интересно, это вообще заработает???????
    private final Map<ConditionTypeAvro, Function<Object, Integer>> valueExtractors = Map.of(
            ConditionTypeAvro.SWITCH, data -> ((SwitchSensorAvro) data).getState() ? 1 : 0,
            ConditionTypeAvro.MOTION, data -> ((MotionSensorAvro) data).getMotion() ? 1 : 0,
            ConditionTypeAvro.TEMPERATURE, data -> ((ClimateSensorAvro) data).getTemperatureC(),
            ConditionTypeAvro.HUMIDITY, data -> ((ClimateSensorAvro) data).getHumidity(),
            ConditionTypeAvro.CO2LEVEL, data -> ((ClimateSensorAvro) data).getCo2Level(),
            ConditionTypeAvro.LUMINOSITY, data -> ((LightSensorAvro) data).getLuminosity()
    );

    public void handle(SensorsSnapshotAvro snapshot) {
        String hubId = snapshot.getHubId();
        Map<String, SensorStateAvro> currentStates = snapshot.getSensorsState();
         /*  Выдержка из ТЗ:
                При каждом изменении состояния хаба (то есть при получении нового снапшота)
                анализатор должен загружать из хранилища все сохранённые сценарии и проверять их
         */
        scenarioRepository.findAllByHubId(hubId).forEach(scenario -> {
            List<Condition> conditions = conditionRepository.findAllByScenarioId(scenario.getId());
            //сценарий должен сработать тогда, когда все его условия выполнены (освещение и движение и т.п.)
            boolean isFulfilled = !conditions.isEmpty() && conditions.stream()
                    .allMatch(cond -> {
                        SensorStateAvro state = currentStates.get(cond.getSensor().getId());
                        if (state == null) return false;

                        Integer currentVal = extractValue(cond, state);
                        return currentVal != null && check(cond, currentVal);
                    });

            if (isFulfilled) {
                log.info("Scenario '{}' fulfilled", scenario.getName());
                actionRepository.findAllByScenarioId(scenario.getId())
                        .forEach(action -> hubRouterClient.sendRequest(action));
            }
        });
    }

    private Integer extractValue(Condition condition, SensorStateAvro state) {
        return Optional.ofNullable(valueExtractors.get(condition.getType()))
                .map(extractor -> {
                    try {
                        return extractor.apply(state.getData());
                    } catch (ClassCastException e) {
                        log.warn("Mismatched sensor type for condition {}", condition.getType());
                        return null;
                    }
                })
                .orElse(null);
    }

    private boolean check(Condition c, Integer currentVal) {
        return operations.getOrDefault(c.getOperation(), (a, b) -> false)
                .test(currentVal, c.getValue());
    }
}