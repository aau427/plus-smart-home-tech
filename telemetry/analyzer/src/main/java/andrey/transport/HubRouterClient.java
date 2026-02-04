package andrey.transport;

import andrey.mapper.Mapper;
import andrey.model.Action;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;


@Service
@Slf4j
@RequiredArgsConstructor

public class HubRouterClient {

    @GrpcClient("hub-router")
    HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouter;
    final Mapper mapper;

    public void sendRequest(Action action) {
        try {
            DeviceActionRequest actionRequest = mapper.toActionRequest(action);
            hubRouter.handleDeviceAction(actionRequest);
        } catch (Exception e) {
            log.error("Error occurred while sending request", e);
        }
    }
}
