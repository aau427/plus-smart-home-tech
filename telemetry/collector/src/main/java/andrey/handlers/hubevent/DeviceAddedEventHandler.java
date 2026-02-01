package andrey.handlers.hubevent;

import andrey.producer.KafkaClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

@Component
public class DeviceAddedEventHandler extends AbstractHubEventHandler<DeviceAddedEventAvro> {

    public DeviceAddedEventHandler(KafkaClient kafkaClient) {
        super(kafkaClient);
    }

    @Override
    protected DeviceAddedEventAvro buildAvroPayLoad(HubEventProto event) {
        DeviceAddedEventProto tmpEvent = event.getDeviceAdded();
        DeviceTypeAvro deviceType = DeviceTypeAvro.valueOf(tmpEvent.getType().name());
        return DeviceAddedEventAvro.newBuilder()
                .setId(tmpEvent.getId())
                .setType(deviceType)
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }
}
