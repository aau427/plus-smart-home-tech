package andrey.exception.warehouse;

import andrey.exception.base.BaseAbstractException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class SpecifiedProductAlreadyInWarehouseException extends BaseAbstractException {
    public SpecifiedProductAlreadyInWarehouseException(UUID productId) {
        super(
                String.format("Conflict: product %s already in WH", productId),
                "Ошибка, товар с таким описанием уже зарегистрирован на складе",
                HttpStatus.BAD_REQUEST
        );
    }
}
