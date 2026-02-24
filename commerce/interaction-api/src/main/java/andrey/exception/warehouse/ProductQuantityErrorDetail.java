package andrey.exception.warehouse;

import andrey.exception.base.BaseAbstractException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Getter
public class ProductQuantityErrorDetail extends BaseAbstractException {
    private final UUID productId;
    private final Long requiredQuantity;
    private final Long availableQuantity;

    public ProductQuantityErrorDetail(UUID productId, Long requiredQuantity, Long availableQuantity) {
        super(
                String.format("Product %s: required %d, available %d", productId, requiredQuantity, availableQuantity),
                "Недостаточно товара на складе",
                HttpStatus.BAD_REQUEST
        );
        this.productId = productId;
        this.requiredQuantity = requiredQuantity;
        this.availableQuantity = availableQuantity;
    }
}
