package andrey.customutility;

import andrey.dto.order.OrderDto;
import andrey.dto.shoppingstore.ProductDto;
import andrey.feignclient.ShoppingMethods;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentCalculator {
    private final ShoppingMethods shoppingStoreClient;
    private static final int SCALE = 2;
    private static final RoundingMode MODE = RoundingMode.HALF_UP;
    private static final BigDecimal FEE_RATE = BigDecimal.valueOf(0.1);

    /* Расчёт стоимости товаров в заказе.
   Функция должна работать по следующему алгоритму:
   необходимо умножить количество каждого товара из заказа
   на его отпускную цену, которую нужно получить из сервиса shopping-store
   по идентификатору.
   Затем следует сложить полученные значения и вернуть результат.
*/
    public BigDecimal calcProductsCost(OrderDto orderDto) {
        return orderDto.getProducts().entrySet().stream()
                .map(entry -> {
                    UUID productId = entry.getKey();
                    Long quantity = entry.getValue();
                    //получить цену товара из внешнего сервиса
                    ProductDto productDto = shoppingStoreClient.getProductById(productId);
                    BigDecimal pricePerUnit = (productDto != null && productDto.getPrice() != null)
                            ? productDto.getPrice()
                            : BigDecimal.ZERO;

                    return pricePerUnit.multiply(BigDecimal.valueOf(quantity))
                            .setScale(SCALE, RoundingMode.HALF_UP);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(SCALE, MODE);
    }

    /*Реализовать этот метод нужно согласно следующему алгоритму:
    - От суммы стоимости всех товаров нужно взять 10% — это будет НДС. Например, если товар стоит 100 рублей, то НДС составит 10 рублей.
    - Налог прибавляем к стоимости товара, получим 110 рублей.
    - Добавляем стоимость доставки — 50 рублей. И в итоге пользователь видит сумму: 160 рублей.
     */
    public BigDecimal calcTotalCost(OrderDto orderDto) {
        BigDecimal productPrice = orderDto.getProductPrice();
        BigDecimal fee = productPrice
                .multiply(FEE_RATE)
                .setScale(SCALE, MODE);
        return productPrice
                .add(fee)
                .add(orderDto.getDeliveryPrice())
                .setScale(SCALE, MODE);
    }
}
