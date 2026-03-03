package andrey.service;

import andrey.dto.shoppingcart.ShoppingCartDto;
import andrey.dto.warehouse.*;
import andrey.exception.warehouse.*;
import andrey.mapper.WhMapper;
import andrey.model.Booking;
import andrey.model.Product;
import andrey.repository.BookingRepository;
import andrey.repository.WhRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WhServiceImpl implements WhService {

    private final WhRepository repository;
    private final BookingRepository bookingRepository;
    private final WhMapper mapper;

    @Transactional
    @Override
    public void addProductToWh(NewProductInWarehouseRequest request) {
        if (repository.existsById(request.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException(request.getProductId());
        }
        Product product = mapper.toEntity(request);
        // При регистрации товара на складе его 0 штук
        product.setQuantity(0L);
        repository.save(product);
        log.info("Новый товар зарегистрирован на складе: {}", product.getProductId());
    }

    @Transactional
    @Override
    public void increaseProductQuantity(AddProductToWarehouseRequest request) {
        Product product = repository.findById(request.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(request.getProductId()));

        product.setQuantity(product.getQuantity() + request.getQuantity());
        log.info("Количество товара {} увеличено на {}. Итого: {}",
                product.getProductId(), request.getQuantity(), product.getQuantity());
        repository.save(product);
    }

    @Override
    public BookedProductsDto checkQuantityForCart(ShoppingCartDto cart) {
        return validateAllAndCalcDimension(cart.getProducts());
    }

    @Override
    public AddressDto getWhAddress() {
        return AddressService.getCurrentAddress();
    }

    /*
        Собрать товары к заказу для подготовки к отправке.
        Метод получает список товаров и идентификатор заказа.
        По нему повторно проверяется наличие заказанных товаров в нужном
         количестве, уменьшается их доступный остаток
         и создаётся Booking
     */
    @Override
    @Transactional
    public BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderRequest request) {
        Map<UUID, Long> requestedProducts = request.getProducts();
        BookedProductsDto bookedProductsDto = validateAllAndCalcDimension(requestedProducts);
        decreaseProductQuantities(requestedProducts);
        Booking newBooking = Booking.builder()
                .bookingId(UUID.randomUUID())
                .orderId(request.getOrderId())
                .products(request.getProducts())
                .build();
        bookingRepository.save(newBooking);
        return bookedProductsDto;
    }

    /*
        Передать товары в доставку
        Метод должен обновить информацию о собранном заказе в базе данных склада:
        добавить в него идентификатор доставки, который вернул сервис доставки,
        присвоить идентификатор доставки во внутреннем хранилище собранных товаров заказа.
     */
    @Override
    @Transactional
    public void sendToDelivery(ShippedToDeliveryRequest request) {
        Booking booking = bookingRepository.findByOrderId(request.getOrderId())
                .orElseThrow(
                        () -> new NoSpecificBookingInWarehouseException(request.getOrderId())
                );

        booking.setDeliveryId(request.getDeliveryId());
        bookingRepository.save(booking);
        log.info("Обновил Booking {}, добавил ссылку на доставку: {}.", booking.getBookingId(), booking.getDeliveryId());
    }

    /* Возврат товара.
       Если товар возвращается на склад, нужно увеличить остаток.
       Соответственно, метод принимает список товаров с количеством и увеличивает доступный остаток.
       Добавлю от себя: В ТЗ большой косяк. в методе возврата есть список товаров, но
       нет ссылки на соответствующее. бронирование. Т.е. согласно ТЗ мы тупо восстановим кол-во
       товаров на складе, а бронирование, в рамках которого они ранее списывались со
       склада так и останется висеть в базе...
     */
    @Override
    @Transactional
    public void returnProducts(Map<UUID, Long> products) {
        List<Product> productsToIncrease = repository.findAllById(products.keySet());
        Set<UUID> foundIds = productsToIncrease.stream()
                .map(Product::getProductId)
                .collect(Collectors.toSet());
        if (productsToIncrease.size() != products.size()) {
            List<UUID> difference = products.keySet().stream()
                    .filter(key -> !foundIds.contains(key))
                    .toList();
            //запихать все ошибки
            NotAllProductsInWareHouseFound mainException = new NotAllProductsInWareHouseFound();
            difference.forEach(
                    productId -> mainException.addSuppressed(new NoSpecifiedProductInWarehouseException(productId))
            );
            throw mainException;
        }

        productsToIncrease.forEach(product -> {
            Long returnQuantity = products.get(product.getProductId());
            if (returnQuantity != null) {
                product.setQuantity(product.getQuantity() + returnQuantity);
                log.debug("Товар {} возвращен: +{}", product.getProductId(), returnQuantity);
            }
        });

        repository.saveAll(productsToIncrease);
    }

    private BookedProductsDto validateAllAndCalcDimension(Map<UUID, Long> products) {
        List<ProductQuantityErrorDetail> quantityErrors = new ArrayList<>();
        BigDecimal totalWeight = BigDecimal.ZERO;
        BigDecimal totalVolume = BigDecimal.ZERO;
        boolean hasFragile = false;

        for (var entry : products.entrySet()) {
            UUID productId = entry.getKey();
            Long quantityInCart = entry.getValue();

            Product product = repository.findById(productId)
                    .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(productId));

            if (product.getQuantity() < quantityInCart) {
                quantityErrors.add(new ProductQuantityErrorDetail(
                        productId,
                        quantityInCart,
                        product.getQuantity()
                ));
            }

            //если есть ошибки, то смысла вычислять уже нет, все равно верну ошиюку
            if (quantityErrors.isEmpty()) {
                // Считаем вес: weight * quantity
                BigDecimal itemWeight = product.getWeight().multiply(BigDecimal.valueOf(quantityInCart));
                totalWeight = totalWeight.add(itemWeight);

                // Считаем объем: (w * h * d) * quantity
                BigDecimal itemVolume = product.getDimension().getWidth()
                        .multiply(product.getDimension().getHeight())
                        .multiply(product.getDimension().getDepth())
                        .multiply(BigDecimal.valueOf(quantityInCart));
                totalVolume = totalVolume.add(itemVolume);

                // Если хоть один хрупкий — вся посылка хрупкая
                if (product.getFragile()) {
                    hasFragile = true;
                }
            }
        }


        if (!quantityErrors.isEmpty()) {
            NotEnoughQuantityException mainException = new NotEnoughQuantityException();
            quantityErrors.forEach(mainException::addSuppressed); // Добавляем все накопленные ошибки в suppressed
            extracted(mainException);
        }

        return BookedProductsDto.builder()
                .deliveryWeight(totalWeight)
                .deliveryVolume(totalVolume)
                .fragile(hasFragile)
                .build();
    }

    private static void extracted(NotEnoughQuantityException mainException) {
        throw mainException;
    }

    private void decreaseProductQuantities(Map<UUID, Long> requestedProducts) {
        List<Product> products = repository.findAllById(requestedProducts.keySet());

        products.forEach(product -> {
            Long quantityToDecrease = requestedProducts.get(product.getProductId());
            product.setQuantity(product.getQuantity() - quantityToDecrease);
        });

        repository.saveAll(products);
        log.debug("Остатки обновлены для {} позиций", requestedProducts.size());
    }
}
