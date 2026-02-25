package andrey.dto.warehouse;

import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class DimensionDto {
    @DecimalMin("1.0")
    private BigDecimal width;
    @DecimalMin("1.0")
    private BigDecimal height;
    @DecimalMin("1.0")
    private BigDecimal depth;
}
