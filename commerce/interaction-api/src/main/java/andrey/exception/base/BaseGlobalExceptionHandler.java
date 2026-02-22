package andrey.exception.base;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public abstract class BaseGlobalExceptionHandler {

    @ExceptionHandler(BaseAbstractException.class)
    public ResponseEntity<BaseAbstractException> handleBaseException(BaseAbstractException ex) {
        return new ResponseEntity<>(ex, ex.getHttpStatus());
    }
}
