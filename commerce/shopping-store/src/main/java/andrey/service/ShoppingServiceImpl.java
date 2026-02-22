package andrey.service;

import andrey.dto.shoppingstore.ProductDto;
import andrey.dto.shoppingstore.SetProductQuantityStateRequest;
import andrey.enums.ProductCategory;
import andrey.enums.ProductState;
import andrey.exception.ValidationException;
import andrey.exception.store.ProductNotFoundException;
import andrey.model.Product;
import andrey.model.ProductMapper;
import andrey.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ShoppingServiceImpl implements ShoppingService {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    public Page<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable) {
        Page<Product> productPage = repository.findAllByProductCategory(category, pageable);
        return productPage.map(mapper::toDto);
    }

    @Transactional
    @Override
    public ProductDto addProduct(ProductDto productDto) {

        Product product = mapper.toEntity(productDto);
        if (product.getProductId() == null) {
            product.setProductId(UUID.randomUUID());
            log.info("Сгенерирован новый ID для товара: {}", product.getProductId());
        }
        Product savedProduct = repository.save(product);
        log.info("Создан новый товар: {} ", savedProduct.getProductName());
        return mapper.toDto(savedProduct);
    }

    @Transactional
    @Override
    public ProductDto updateProduct(ProductDto productDto) {

        if (productDto.getProductId() == null) {
            throw new ValidationException(
                    "Product ID is required",
                    "Не указан Id товара");
        }

        Product product = mapper.toEntity(productDto);

        if (!repository.existsById(product.getProductId())) {
            throw new ProductNotFoundException(product.getProductId());
        }

        Product updatedProduct = repository.save(product);
        log.info("Товар {} обновлен.", updatedProduct.getProductName());
        return mapper.toDto(updatedProduct);
    }

    @Transactional
    @Override
    public boolean removeProduct(UUID productId) {
        Product product = getProductByIdCustom(productId);
        product.setProductState(ProductState.DEACTIVATE);
        repository.save(product);
        log.info("Продукт  {} удален", product.getProductName());
        return true;
    }

    @Transactional
    @Override
    public boolean updateQuantityState(SetProductQuantityStateRequest request) {
        Product product = getProductByIdCustom(request.getProductId());

        product.setQuantityState(request.getQuantityState());
        repository.save(product);
        log.info("Изменил статус количества (QuantityState) для товара : {}", product.getProductId());
        return true;
    }

    @Override
    public ProductDto getProductById(UUID productId) {
        Product product = repository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("Товар Id = " + productId + " не найден!")
        );
        return mapper.toDto(product);
    }

    @Override
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    private Product getProductByIdCustom(UUID productId) {
        return repository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("Товар " + productId + " не найден!")
        );
    }
}
