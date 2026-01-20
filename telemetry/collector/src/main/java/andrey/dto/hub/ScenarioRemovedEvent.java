package andrey.dto.hub;

import andrey.dto.enums.HubEventType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@Getter
@Setter
public class ScenarioRemovedEvent extends HubEvent {
    @NotBlank(message = "Наименование сценария не может быть пустым")
    private String name;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
