package andrey.exception.warehouse;

import andrey.exception.base.BaseAbstractException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class NoSpecificBookingInWarehouseException extends BaseAbstractException {
    public NoSpecificBookingInWarehouseException(UUID orderId) {
        super(
                String.format("There is no information about the booking for order %s in warehouse.", orderId),
                "Нет информации о бронировании на складе.",
                HttpStatus.BAD_REQUEST
        );
    }
}
