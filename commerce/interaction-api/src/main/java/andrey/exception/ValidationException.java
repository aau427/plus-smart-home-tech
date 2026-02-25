package andrey.exception;

import andrey.exception.base.BaseAbstractException;
import org.springframework.http.HttpStatus;

public class ValidationException extends BaseAbstractException {
    public ValidationException(String techMessage, String userMessage) {
        super(techMessage, userMessage, HttpStatus.BAD_REQUEST);
    }
}
