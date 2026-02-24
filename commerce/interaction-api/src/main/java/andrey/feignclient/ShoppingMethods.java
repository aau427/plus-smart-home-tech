package andrey.feignclient;

import andrey.dto.shoppingstore.ProductDto;
import andrey.dto.shoppingstore.SetProductQuantityStateRequest;
import andrey.enums.ProductCategory;
import andrey.enums.QuantityState;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingMethods {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<ProductDto> getProductsByCategory(
            @RequestParam ProductCategory category,
            @PageableDefault(sort = "productName", direction = Sort.Direction.ASC) Pageable pageable);

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    ProductDto createProduct(@Valid @RequestBody ProductDto productDto);

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    ProductDto updateProduct(@Valid @RequestBody ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    @ResponseStatus(HttpStatus.OK)
    public boolean removeProduct(@Valid @RequestBody UUID productId);

    @PostMapping("/quantityState")
    @ResponseStatus(HttpStatus.OK)
    boolean updateQuantityState(@Valid @RequestBody SetProductQuantityStateRequest request);

    //в тестах postman косях - там ID товара и quantityState передаются в параметрах запроса, что противоречит swagger
    //чтобы пройти тесты добавил отдельный метод для обработки урла с параметрами.
    @PostMapping(value = "/quantityState", params = {"productId", "quantityState"})
    @ResponseStatus(HttpStatus.OK)
    public boolean updateQuantityStateParams(
            @RequestParam UUID productId,
            @RequestParam QuantityState quantityState);

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getProductById(@PathVariable UUID productId);
}
