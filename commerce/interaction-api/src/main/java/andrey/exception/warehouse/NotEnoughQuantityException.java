package andrey.exception.warehouse;

import andrey.exception.base.BaseAbstractException;
import org.springframework.http.HttpStatus;

public class NotEnoughQuantityException extends BaseAbstractException {
    public NotEnoughQuantityException() {
        super(
                "Not enough quantity for products in warehouse, see suppressed",
                "Ошибка, товар из корзины не находится в требуемом количестве на складе",
                HttpStatus.BAD_REQUEST);
    }
}
