package andrey.controller;

import andrey.dto.shoppingcart.ShoppingCartDto;
import andrey.dto.warehouse.AddressDto;
import andrey.dto.warehouse.AssemblyProductsForOrderRequest;
import andrey.dto.warehouse.BookedProductsDto;
import andrey.dto.warehouse.ShippedToDeliveryRequest;
import andrey.feignclient.WhSharedMethods;
import andrey.service.WhService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

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

    //собрать товары к заказу для подготовки к отправке
    @Override
    @PostMapping("/assembly")
    @ResponseStatus(HttpStatus.OK)
    public BookedProductsDto assemblyProductsForOrder(@RequestBody @Valid AssemblyProductsForOrderRequest request) {
        return service.assemblyProductsForOrder(request);
    }

    //передать товары в доставку
    @Override
    @PostMapping("/shipped")
    @ResponseStatus(HttpStatus.OK)
    public void sendToDelivery(@Valid @RequestBody ShippedToDeliveryRequest request) {
        service.sendToDelivery(request);
    }

    //Принять возврат товаров на склад
    @Override
    @PostMapping("/return")
    @ResponseStatus(HttpStatus.OK)
    public void returnProducts(@RequestBody Map<UUID, Long> products) {
        service.returnProducts(products);
    }

}
