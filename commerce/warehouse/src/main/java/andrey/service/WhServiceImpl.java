package andrey.service;

import andrey.dto.shoppingcart.ShoppingCartDto;
import andrey.dto.warehouse.AddProductToWarehouseRequest;
import andrey.dto.warehouse.AddressDto;
import andrey.dto.warehouse.BookedProductsDto;
import andrey.dto.warehouse.NewProductInWarehouseRequest;
import andrey.exception.warehouse.NoSpecifiedProductInWarehouseException;
import andrey.exception.warehouse.NotEnoughQuantityException;
import andrey.exception.warehouse.ProductQuantityErrorDetail;
import andrey.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;
import andrey.mapper.WhMapper;
import andrey.model.Product;
import andrey.repository.WhRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WhServiceImpl implements WhService {

    private final WhRepository repository;
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
        List<ProductQuantityErrorDetail> quantityErrors = new ArrayList<>();
        BigDecimal totalWeight = BigDecimal.ZERO;
        BigDecimal totalVolume = BigDecimal.ZERO;
        boolean hasFragile = false;

        for (var entry : cart.getProducts().entrySet()) {
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
            throw mainException;
        }

        return BookedProductsDto.builder()
                .deliveryWeight(totalWeight)
                .deliveryVolume(totalVolume)
                .fragile(hasFragile)
                .build();
    }

    @Override
    public AddressDto getWhAddress() {
        return AddressService.getCurrentAddress();
    }
}
