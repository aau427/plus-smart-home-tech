package andrey.exception.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/*
    Чтобы не плодить «говнокод» с кучей одинаковых полей в каждом классе,
    сделаем базовый абстрактный класс, а конкретные ошибки будут просто наследоваться от него.
 */
@Getter
public abstract class BaseAbstractException extends RuntimeException {
    private final String userMessage;
    private final HttpStatus httpStatus;

    protected BaseAbstractException(String techMessage, String userMessage, HttpStatus httpStatus) {
        super(techMessage, new RuntimeException(techMessage));
        this.userMessage = userMessage;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getLocalizedMessage() {
        return this.userMessage;
    }
}
