package andrey.model;

import andrey.enums.ProductCategory;
import andrey.enums.ProductState;
import andrey.enums.QuantityState;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products", schema = "store")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "productId")
public class Product {
    @Id
    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_src", length = 512)
    private String imageSrc;

    @Enumerated(EnumType.STRING)
    @Column(name = "quantity_state", nullable = false, length = 50)
    private QuantityState quantityState;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_state", nullable = false, length = 50)
    private ProductState productState;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_category", length = 50)
    private ProductCategory productCategory = ProductCategory.NOT_SPECIFIED;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}
