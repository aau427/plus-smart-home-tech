package andrey.exception.payment;

import andrey.exception.base.BaseAbstractException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class PaymentForOrderAlreadyExists extends BaseAbstractException {
    public PaymentForOrderAlreadyExists(UUID orderId) {
        super(
                String.format("Conflict: payment for order %s already exists", orderId),
                "Оплата для указанного заказа уже зарегистрована",
                HttpStatus.BAD_REQUEST
        );
    }
}
