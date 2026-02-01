package andrey.handlers.hubevent;

import andrey.producer.KafkaClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScenarioAddedEventHandler extends AbstractHubEventHandler<ScenarioAddedEventAvro> {
    public ScenarioAddedEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    protected ScenarioAddedEventAvro buildAvroPayLoad(HubEventProto event) {
        final ScenarioAddedEventProto tmpEvent = event.getScenarioAdded();

        return ScenarioAddedEventAvro.newBuilder()
                .setName(tmpEvent.getName())
                .setActions(mapActions(tmpEvent.getActionList()))
                .setConditions(mapConditions(tmpEvent.getConditionList()))
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    private List<ScenarioConditionAvro> mapConditions(List<ScenarioConditionProto> conditions) {
        return conditions.stream()
                .map(this::mapCondition)
                .collect(Collectors.toList());
    }

    private ScenarioConditionAvro mapCondition(ScenarioConditionProto condition) {
        var builder = ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setType(ConditionTypeAvro.valueOf(condition.getType().name()))
                .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().name()));

        switch (condition.getValueCase()) {
            case BOOL_VALUE:
                builder.setValue(condition.getBoolValue());
                break;
            case INT_VALUE:
                builder.setValue(condition.getIntValue());
                break;
            default:
                builder.setValue(null);
        }

        return builder.build();
    }

    private List<DeviceActionAvro> mapActions(List<DeviceActionProto> actions) {
        return actions.stream()
                .map(this::mapAction)
                .collect(Collectors.toList());
    }

    private DeviceActionAvro mapAction(DeviceActionProto action) {
        DeviceActionAvro.Builder deviceActionAvro = DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(ActionTypeAvro.valueOf(action.getType().name()));
        if (action.hasIntValue()) {
            deviceActionAvro.setValue(action.getIntValue());
        } else {
            deviceActionAvro.setValue(null);
        }

        return deviceActionAvro.build();
    }
}
