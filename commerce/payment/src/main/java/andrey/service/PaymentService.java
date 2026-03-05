package andrey.service;

import andrey.dto.order.OrderDto;
import andrey.dto.payment.PaymentDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    PaymentDto createOrderPayment(OrderDto orderDto);

    BigDecimal calcOrderTotalCost(OrderDto orderDto);

    void confirmPayment(UUID paymentId);

    BigDecimal calcProductCostInOrder(OrderDto orderDto);

    void cancelPayment(@RequestBody @NotNull UUID paymentId);
}


