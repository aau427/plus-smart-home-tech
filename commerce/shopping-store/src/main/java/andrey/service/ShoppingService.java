package andrey.service;

import andrey.dto.shoppingstore.ProductDto;
import andrey.dto.shoppingstore.SetProductQuantityStateRequest;
import andrey.enums.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ShoppingService {

    /**
     * Получение страницы товаров, отфильтрованных по категории.
     */
    Page<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable);

    /**
     * Добавление нового товара в магазин.
     * Соответствует методу PUT в контроллере.
     * Именно put!!!! так написано в swagger!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * хотя должно быть post.
     */
    ProductDto addProduct(ProductDto productDto);

    /**
     * Обновление данных существующего товара.
     * Соответствует методу POST в контроллере (так захотел автор задания!!!!)
     */
    ProductDto updateProduct(ProductDto productDto);

    /**
     * Удаление товара из магазина по его ID.
     */
    boolean removeProduct(UUID productId);

    /**
     * Обновление состояния остатков товара (QuantityState).
     */
    boolean updateQuantityState(SetProductQuantityStateRequest request);

    /**
     * Получение детальной информации о товаре по его ID.
     */
    ProductDto getProductById(UUID productId);

    /**
     * Получение страницы всех товаров без фильтрации.
     */
    Page<ProductDto> getAllProducts(Pageable pageable);
}
