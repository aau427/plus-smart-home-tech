package andrey.exception.warehouse;

import andrey.exception.base.BaseAbstractException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class NoSpecifiedProductInWarehouseException extends BaseAbstractException {
    public NoSpecifiedProductInWarehouseException(UUID productId) {
        super(
                String.format("There is no information about the product %s in warehouse.", productId),
                "Нет информации о товаре на складе.",
                HttpStatus.BAD_REQUEST
        );
    }
}
