package andrey.model;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class Dimension {

    @Column(name = "width", nullable = false)
    private BigDecimal width;

    @Column(name = "height", nullable = false)
    private BigDecimal height;

    @Column(name = "depth", nullable = false)
    private BigDecimal depth;
}
