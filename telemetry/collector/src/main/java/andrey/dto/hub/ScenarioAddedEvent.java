package andrey.dto.hub;

import andrey.dto.enums.HubEventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString(callSuper = true)
@Setter
@Getter
public class ScenarioAddedEvent extends HubEvent {
    @NotBlank(message = "Наименование сценария не может быть пустым")
    private String name;

    @NotNull(message = "Список условий, связанных со сценариями, не может быть пустым")
    private List<ScenarioCondition> conditions;

    @NotNull(message = "Список действий, которые должны быть выполнены в рамках сценария, не может быть пустым")
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
