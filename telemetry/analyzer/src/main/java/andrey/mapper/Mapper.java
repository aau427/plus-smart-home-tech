package andrey.mapper;

import andrey.model.Action;
import andrey.model.Sensor;
import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@Component
public class Mapper {
    public DeviceActionRequest toActionRequest(Action action) {
        return DeviceActionRequest.newBuilder()
                .setHubId(action.getScenario().getHubId())
                .setScenarioName(action.getScenario().getName())
                .setAction(toDeviceActionProto(action))
                .setTimestamp(toTimestamp())
                .build();
    }

    private DeviceActionProto toDeviceActionProto(Action action) {
        DeviceActionProto.Builder builder = DeviceActionProto.newBuilder()
                .setSensorId(action.getSensor().getId())
                .setType(toActionTypeProto(action.getType()));
        if (action.getValue() != null) {
            builder.setIntValue(action.getValue());
        }

        return builder.build();
    }

    public Sensor avroToSensor(HubEventAvro event) {
        DeviceAddedEventAvro deviceAddedEventAvro = (DeviceAddedEventAvro) event.getPayload();
        Sensor sensor = new Sensor();
        sensor.setHubId(event.getHubId());
        sensor.setId(deviceAddedEventAvro.getId());
        return sensor;
    }

    private Timestamp toTimestamp() {
        Instant instant = Instant.now();
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }

    private ActionTypeProto toActionTypeProto(ActionTypeAvro actionType) {
        return switch (actionType) {
            case ACTIVATE -> ActionTypeProto.ACTIVATE;
            case DEACTIVATE -> ActionTypeProto.DEACTIVATE;
            case INVERSE -> ActionTypeProto.INVERSE;
            case SET_VALUE -> ActionTypeProto.SET_VALUE;
        };
    }
}
