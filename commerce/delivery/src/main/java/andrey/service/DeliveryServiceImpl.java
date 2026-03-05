package andrey.service;

import andrey.customutility.Calculator;
import andrey.dto.delivery.DeliveryDto;
import andrey.dto.order.OrderDto;
import andrey.dto.warehouse.ShippedToDeliveryRequest;
import andrey.enums.DeliveryState;
import andrey.exception.delivery.NoDeliveryFoundException;
import andrey.exception.delivery.SpecifiedDeliveryAlreadyExists;
import andrey.feignclient.OrderMethods;
import andrey.feignclient.WhSharedMethods;
import andrey.mapper.DeliveryMapper;
import andrey.model.Delivery;
import andrey.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryMapper deliveryMapper;
    private final DeliveryRepository deliveryRepository;
    private final OrderMethods orderClient;
    private final WhSharedMethods warehouseClient;
    private final Calculator calculator;

    @Override
    @Transactional
    public DeliveryDto planDelivery(DeliveryDto request) {
        log.debug("Получен запрос на создание доставки {} ", request);
        if (deliveryRepository.existsByOrderId(request.getOrderId())) {
            throw new SpecifiedDeliveryAlreadyExists(request.getOrderId());
        }
        Delivery iDelivery = deliveryMapper.toEntity(request);
        Delivery savedDelivery = deliveryRepository.save(iDelivery);
        log.debug("Зарегистрирована доставка {}", savedDelivery);
        return deliveryMapper.toDto(savedDelivery);
    }

    //эмуляция успешной доставки товара
    @Transactional
    @Override
    public void confirmDelivery(UUID orderId) {
        Delivery iDelivery = getDeliveryByOrderId(orderId);
        iDelivery.setDeliveryState(DeliveryState.DELIVERED);
        orderClient.delivery(orderId);

        deliveryRepository.save(iDelivery);
        log.debug("Заказ успешно доставлен {}", orderId);
    }

    //эмуляция получения товара в доставку
    @Transactional
    @Override
    public void setDeliveryPicked(UUID orderId) {
        Delivery iDelivery = getDeliveryByOrderId(orderId);

        //1. Поменять статус самой доставки на IN_PROGRESS
        iDelivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        //2. В соответствии с ТЗ: "Также необходимо изменить статус заказа на ASSEMBLED в сервисе заказов"
        orderClient.assembleOrder(orderId);
        //3. В соотв. с ТЗ: связать идентификатор доставки с внутренней учётной системой через вызов соответствующего метода склада
        ShippedToDeliveryRequest request = ShippedToDeliveryRequest.builder()
                .orderId(orderId)
                .deliveryId(iDelivery.getDeliveryId())
                .build();
        warehouseClient.sendToDelivery(request);

        deliveryRepository.save(iDelivery);
        log.debug("Заказ {} получен в доставку", orderId);
    }

    @Transactional
    @Override
    public void setDeliveryFailed(UUID orderId) {
        Delivery iDelivery = getDeliveryByOrderId(orderId);
        //1. Поменять статус доставки
        iDelivery.setDeliveryState(DeliveryState.FAILED);
        //2. Пометить сам заказ
        orderClient.deliveryFailed(orderId);
        deliveryRepository.save(iDelivery);
    }

    @Override
    public BigDecimal calcTotalDeliveryPrice(OrderDto orderDto) {
        Delivery iDelivery = deliveryRepository.findById(orderDto.getDeliveryId())
                .orElseThrow(
                        () -> new NoDeliveryFoundException(orderDto.getOrderId())
                );
        return calculator.calculateDeliveryCost(orderDto, iDelivery);

    }

    private Delivery getDeliveryByOrderId(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(
                        () -> new NoDeliveryFoundException(orderId)
                );

    }
}
