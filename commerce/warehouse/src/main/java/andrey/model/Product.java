package andrey.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(schema = "wh", name = "products")
@EqualsAndHashCode(of = "productId")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "fragile", nullable = false)
    private Boolean fragile;

    @Embedded
    private Dimension dimension;

    @Column(name = "weight", nullable = false)
    private BigDecimal weight;

    @Column(name = "quantity", nullable = false)
    private Long quantity;
}
