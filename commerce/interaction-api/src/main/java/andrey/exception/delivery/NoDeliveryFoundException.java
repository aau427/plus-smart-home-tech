package andrey.exception.delivery;

import andrey.exception.base.BaseAbstractException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class NoDeliveryFoundException extends BaseAbstractException {
    public NoDeliveryFoundException(UUID orderId) {
        super(
                String.format("Conflict: delivery for order %s not found", orderId),
                "Не найдена доставка для указанного товара",
                HttpStatus.BAD_REQUEST
        );
    }
}