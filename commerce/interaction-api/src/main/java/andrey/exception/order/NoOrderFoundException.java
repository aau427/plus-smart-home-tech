package andrey.exception.order;

import andrey.exception.base.BaseAbstractException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class NoOrderFoundException extends BaseAbstractException {
    public NoOrderFoundException(UUID orderId) {
        super(
                String.format("Order %s not found", orderId),
                "Не найден заказ",
                HttpStatus.BAD_REQUEST
        );
    }
}