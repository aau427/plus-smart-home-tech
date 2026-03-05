package andrey.exception.warehouse;

import andrey.exception.base.BaseAbstractException;
import org.springframework.http.HttpStatus;

public class NotAllProductsInWareHouseFound extends BaseAbstractException {
    public NotAllProductsInWareHouseFound() {
        super(
                "Not all products found in warehouse, see suppressed",
                "Ошибка, не все товары найдены в хранилище",
                HttpStatus.BAD_REQUEST);
    }
}
