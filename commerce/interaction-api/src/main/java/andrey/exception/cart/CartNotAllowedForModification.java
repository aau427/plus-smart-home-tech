package andrey.exception.cart;

import andrey.exception.base.BaseAbstractException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class CartNotAllowedForModification extends BaseAbstractException {
    public CartNotAllowedForModification(UUID cartId) {
        super(
                String.format("Shopping cart %s deactivated and not allowed for modification", cartId),
                "Корзина удалена и недоступна для изменения",
                HttpStatus.GONE);
    }
}
