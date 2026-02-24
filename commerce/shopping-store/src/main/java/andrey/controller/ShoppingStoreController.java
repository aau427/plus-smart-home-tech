package andrey.controller;

import andrey.dto.shoppingstore.ProductDto;
import andrey.dto.shoppingstore.SetProductQuantityStateRequest;
import andrey.enums.ProductCategory;
import andrey.enums.QuantityState;
import andrey.feignclient.ShoppingMethods;
import andrey.service.ShoppingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
@Validated
public class ShoppingStoreController implements ShoppingMethods {

    private final ShoppingService service;

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductDto> getProductsByCategory(
            @RequestParam ProductCategory category,
            @PageableDefault(sort = "productName", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.getProductsByCategory(category, pageable);
    }

    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createProduct(@Valid @RequestBody ProductDto productDto) {
        return service.addProduct(productDto);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) {
        return service.updateProduct(productDto);
    }

    @Override
    @PostMapping("/removeProductFromStore")
    @ResponseStatus(HttpStatus.OK)
    public boolean removeProduct(@Valid @RequestBody UUID productId) {
        return service.removeProduct(productId);
    }

    @Override
    @PostMapping("/quantityState")
    @ResponseStatus(HttpStatus.OK)
    public boolean updateQuantityState(@Valid @RequestBody SetProductQuantityStateRequest request) {
        return service.updateQuantityState(request);
    }

    @Override
    //в тестах postman косях - там ID товара и quantityState передаются в параметрах запроса, что противоречит swagger
    //чтобы пройти тесты добавил отдельный метод для обработки урла с параметрами.
    @PostMapping(value = "/quantityState", params = {"productId", "quantityState"})
    @ResponseStatus(HttpStatus.OK)
    public boolean updateQuantityStateParams(
            @RequestParam UUID productId,
            @RequestParam QuantityState quantityState) {
        return service.updateQuantityState(new SetProductQuantityStateRequest(productId, quantityState));
    }

    @Override
    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getProductById(@PathVariable UUID productId) {
        return service.getProductById(productId);
    }
}
