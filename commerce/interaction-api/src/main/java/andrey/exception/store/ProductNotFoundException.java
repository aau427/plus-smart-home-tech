package andrey.exception.store;

import andrey.exception.base.BaseAbstractException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ProductNotFoundException extends BaseAbstractException {
    public ProductNotFoundException(UUID productId) {
        super(
                String.format("Product %s not found", productId),
                "Ошибка, товар по идентификатору в БД не найден",
                HttpStatus.NOT_FOUND);
    }
}
