package andrey.exception.delivery;

import andrey.exception.base.BaseAbstractException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class SpecifiedDeliveryAlreadyExists extends BaseAbstractException {
    public SpecifiedDeliveryAlreadyExists(UUID orderId) {
        super(
                String.format("Conflict: delivery for order %s already exists", orderId),
                "Доставка для указанного заказа уже зарегистрована",
                HttpStatus.BAD_REQUEST
        );
    }
}
