package andrey;

import andrey.exception.UnknownEventException;
import andrey.handlers.hubevent.HubEventHandler;
import andrey.handlers.sensorevent.SensorEventHandler;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@GrpcService
@Slf4j
public class EventController extends CollectorControllerGrpc.CollectorControllerImplBase {

    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> sensorEventHandlers;

    private final Map<HubEventProto.PayloadCase, HubEventHandler> hubEventHandlers;

    /*
        паттерн "Стратегия". Важно и прикольно: все реализации интерфейсов SensorEventHandler
        и HubEventHandler, отмеченные как @Component встроятся через конструктор!
     */
    public EventController(final Set<SensorEventHandler> setOfSensorsEventHandlers,
                           final Set<HubEventHandler> setOfHubEventHandlers) {
        sensorEventHandlers = setOfSensorsEventHandlers.stream()
                .collect(Collectors.toMap(sensorEventHandler -> sensorEventHandler.getMessageType(),
                        Function.identity()));
        hubEventHandlers = setOfHubEventHandlers.stream()
                .collect(Collectors.toMap(hubEventHandler -> hubEventHandler.getMessageType(),
                        Function.identity()));
    }

    @Override
    public void collectSensorEvent(final SensorEventProto request,
                                   final StreamObserver<Empty> responseObserver) {
        log.info("Получил событие датчика: sensorId={}, hubId={} типа {}",
                request.getId(),
                request.getHubId(),
                request.getPayloadCase());
        try {
            if (!sensorEventHandlers.containsKey(request.getPayloadCase())) {
                throw new UnknownEventException("Не могу найти обработчик для события датчика типа " +
                        request.getPayloadCase());
            }
            sensorEventHandlers.get(request.getPayloadCase()).process(request);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }

    @Override
    public void collectHubEvent(final HubEventProto request,
                                final StreamObserver<Empty> responseObserver) {
        log.info("Получил событие хаба: hubId={} типа {}",
                request.getHubId(),
                request.getPayloadCase());
        try {
            if (!hubEventHandlers.containsKey(request.getPayloadCase())) {
                throw new UnknownEventException("Не могу найти обработчик для события хаба типа " +
                        request.getPayloadCase());
            }
            hubEventHandlers.get(request.getPayloadCase()).process(request);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }
}
