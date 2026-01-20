package andrey.dto.hub;

import andrey.dto.enums.HubEventType;

public class UnknownHubEvent extends HubEvent {
    @Override
    public HubEventType getType() {
        return HubEventType.UNKNOWN;
    }
}
