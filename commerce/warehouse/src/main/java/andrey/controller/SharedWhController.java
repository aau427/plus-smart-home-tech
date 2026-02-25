package andrey.controller;

import andrey.dto.shoppingcart.ShoppingCartDto;
import andrey.dto.warehouse.AddressDto;
import andrey.dto.warehouse.BookedProductsDto;
import andrey.feignclient.WhSharedMethods;
import andrey.service.WhService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/warehouse")
@Validated
@RequiredArgsConstructor
public class SharedWhController implements WhSharedMethods {

    private final WhService service;

    //Предварительно проверить что количество товаров на складе достаточно для данной корзиный продуктов.
    @Override
    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public BookedProductsDto checkQuantityForCart(@Valid @RequestBody ShoppingCartDto cart) {
        return service.checkQuantityForCart(cart);
    }

    @Override
    @GetMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    public AddressDto getWhAddress() {
        return service.getWhAddress();
    }
}
