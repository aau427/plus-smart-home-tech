package andrey.exception.payment;

import andrey.exception.base.BaseAbstractException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class PaymentNotFoundException extends BaseAbstractException {
    public PaymentNotFoundException(UUID paymentId) {
        super(
                String.format("Conflict: payment %s not exists", paymentId),
                "Заказ не зарегистрован",
                HttpStatus.BAD_REQUEST
        );
    }
}
