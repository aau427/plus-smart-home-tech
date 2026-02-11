package andrey.handlers.sensorevent;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface SensorEventHandler {

    SensorEventProto.PayloadCase getMessageType();

    void process(SensorEventProto event);
}
