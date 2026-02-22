package andrey.exception.warehouse;

import andrey.exception.base.BaseAbstractException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class NotEnoughQuantityException extends BaseAbstractException {
    public NotEnoughQuantityException(UUID productId) {
        super(
                String.format("Not enough quantity for product %s in warehouse", productId),
                "Ошибка, товар из корзины не находится в требуемом количестве на складе",
                HttpStatus.BAD_REQUEST);
    }
}
