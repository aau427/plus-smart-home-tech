package andrey.factory;

import andrey.handler.hub.HubEventHandler;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class HubEventFactory {
    //Магия!!!
    private final Map<String, HubEventHandler> handlers;

    public HubEventFactory(Set<HubEventHandler> setOfHandlers) {
        this.handlers = setOfHandlers.stream()
                .collect(Collectors.toUnmodifiableMap(
                        HubEventHandler::getType,
                        h -> h
                ));
    }

    public Optional<HubEventHandler> getHandlerForHubEvent(String eventType) {
        return Optional.ofNullable(handlers.get(eventType));
    }
}
