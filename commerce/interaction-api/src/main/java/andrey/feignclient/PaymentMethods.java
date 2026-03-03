package andrey.feignclient;

import andrey.dto.order.OrderDto;
import andrey.dto.payment.PaymentDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentMethods {
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    PaymentDto createOrderPayment(@RequestBody @Valid OrderDto orderDto);

    //Расчет полной стоимости заказа
    @PostMapping("/totalCost")
    @ResponseStatus(HttpStatus.OK)
    BigDecimal calcOrderTotalCost(@RequestBody @Valid OrderDto orderDto);

    //успешная оплата в платежной шлюзе
    @PostMapping("/refund")
    @ResponseStatus(HttpStatus.OK)
    public void confirmPayment(@RequestBody UUID paymentId);

    //Расчёт стоимости товаров в заказе.
    @PostMapping("/productCost")
    @ResponseStatus(HttpStatus.OK)
    BigDecimal calcProductCostInOrder(@RequestBody @Valid OrderDto orderDto);

    //отказ в оплате
    @PostMapping("/failed")
    @ResponseStatus(HttpStatus.OK)
    void cancelPayment(@RequestBody @NotNull UUID paymentId);
}
