package andrey.customutility;

import andrey.dto.order.OrderDto;
import andrey.model.Delivery;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
/*
    Тварь я дрожащая или право имею? (CR Ф.М.Достоевский)
     добавим, так сказать, экзистенциальную глубину в виде конвеера:)
 */


@Component
// Константы тарифов
public class Calculator {
    private static final BigDecimal BASE_COST = BigDecimal.valueOf(5.0);
    private static final BigDecimal ADDR_2_COEFF = BigDecimal.valueOf(2.0);
    private static final BigDecimal ADDR_1_COEFF = BigDecimal.valueOf(1.0);
    private static final BigDecimal FRAGILE_COEFF = BigDecimal.valueOf(0.2);
    private static final BigDecimal WEIGHT_COEFF = BigDecimal.valueOf(0.3);
    private static final BigDecimal VOLUME_COEFF = BigDecimal.valueOf(0.2);
    private static final BigDecimal DISTANCE_COEFF = BigDecimal.valueOf(0.2);
    private static final int SCALE = 2;

    //используем школьное округление
    private final UnaryOperator<BigDecimal> round = v -> v.setScale(SCALE, RoundingMode.HALF_UP);

    private final Function<String, BigDecimal> getWarehouseCoeff = addr ->
            addr.contains("ADDRESS_2") ? ADDR_2_COEFF : ADDR_1_COEFF;

    public BigDecimal calculateDeliveryCost(final OrderDto order, final Delivery delivery) {
        //1. стартуем с базовой стоимости
        return Optional.of(BASE_COST)
                //2. накручиваем за адрес
                .map(cost -> {
                    BigDecimal coeff = getWarehouseCoeff.apply(delivery.getCountryFrom());
                    BigDecimal addCost = round.apply(cost.multiply(coeff));
                    return round.apply(cost.add(addCost));
                })

                // 3. накручиваем за хрупкость
                .map(cost ->
                        order.isFragile()
                                ? round.apply(cost.add(round.apply(cost.multiply(FRAGILE_COEFF))))
                                : cost)
                // 4. накручиваем за вес
                .map(cost ->
                        round.apply(cost.add(round.apply(order.getDeliveryWeight().multiply(WEIGHT_COEFF)))))

                // 5. Накручиваем за объем
                .map(cost -> round.apply(cost.add(
                        round.apply(order.getDeliveryVolume().multiply(VOLUME_COEFF)))))

                // 5. Накручиваем за разные улица (если разные улицы)
                .map(cost ->
                        !delivery.getStreetFrom().equals(delivery.getStreetTo())
                                ? round.apply(cost.add(round.apply(cost.multiply(DISTANCE_COEFF))))
                                : cost)
                .orElse(BigDecimal.ZERO);
    }
}
