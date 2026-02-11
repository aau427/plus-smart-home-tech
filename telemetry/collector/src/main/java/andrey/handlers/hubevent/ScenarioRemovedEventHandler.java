package andrey.handlers.hubevent;

import andrey.producer.KafkaClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
public class ScenarioRemovedEventHandler extends AbstractHubEventHandler<ScenarioRemovedEventAvro> {

    public ScenarioRemovedEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    protected ScenarioRemovedEventAvro buildAvroPayLoad(HubEventProto event) {
        final ScenarioRemovedEventProto tmpEvent = event.getScenarioRemoved();
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(tmpEvent.getName())
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }
}
