package andrey.exception.payment;

import andrey.exception.base.BaseAbstractException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class NotEnoughInfoInOrderToCalculateException extends BaseAbstractException {
    public NotEnoughInfoInOrderToCalculateException(UUID orderId) {
        super(
                String.format("Not enough information in order %s to calculate cost", orderId),
                "Недостаточно информации в заказе для расчёта",
                HttpStatus.BAD_REQUEST
        );
    }
}
