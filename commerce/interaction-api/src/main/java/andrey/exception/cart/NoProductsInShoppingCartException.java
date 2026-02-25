package andrey.exception.cart;

import andrey.exception.base.BaseAbstractException;
import org.springframework.http.HttpStatus;

public class NoProductsInShoppingCartException extends BaseAbstractException {
    public NoProductsInShoppingCartException(String userName) {
        super(
                String.format("No matching products in cart for user %s", userName),
                "Нет искомых товаров в корзине",
                HttpStatus.BAD_REQUEST
        );
    }
}
