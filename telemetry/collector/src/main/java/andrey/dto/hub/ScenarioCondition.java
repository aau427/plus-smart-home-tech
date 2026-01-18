package andrey.dto.hub;

import andrey.dto.enums.ConditionOperation;
import andrey.dto.enums.ConditionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ScenarioCondition {
    @NotBlank(message = "Идентификатор датчика не может быть пустым!")
    private String sensorId;

    @NotNull(message = "Тип условия не может быть пустым")
    private ConditionType type;

    @NotNull(message = "Операции для условий не могут быть пустыми")
    private ConditionOperation operation;

    /*
        согласно тз: значение для сравнения в условиях.
         Может быть не задано, если неприменимо (null) ...
     */

    private Integer value;
}
