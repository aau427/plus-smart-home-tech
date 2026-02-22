package andrey.exception.cart;

import andrey.exception.base.BaseAbstractException;
import org.springframework.http.HttpStatus;

public class NotAuthorizedUserException extends BaseAbstractException {
    public NotAuthorizedUserException(String techMessage, String userMessage) {
        super(techMessage, userMessage, HttpStatus.UNAUTHORIZED);
    }
}
