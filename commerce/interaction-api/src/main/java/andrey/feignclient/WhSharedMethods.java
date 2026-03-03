package andrey.feignclient;

import andrey.dto.shoppingcart.ShoppingCartDto;
import andrey.dto.warehouse.AddressDto;
import andrey.dto.warehouse.AssemblyProductsForOrderRequest;
import andrey.dto.warehouse.BookedProductsDto;
import andrey.dto.warehouse.ShippedToDeliveryRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "warehouse", contextId = "warehouseShared", path = "/api/v1/warehouse")
public interface WhSharedMethods {
    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    BookedProductsDto checkQuantityForCart(@Valid @RequestBody ShoppingCartDto cart);


    @GetMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    AddressDto getWhAddress();

    @PostMapping("/assembly")
    @ResponseStatus(HttpStatus.OK)
    BookedProductsDto assemblyProductsForOrder(@RequestBody @Valid AssemblyProductsForOrderRequest request);

    @PostMapping("/shipped")
    @ResponseStatus(HttpStatus.OK)
    void sendToDelivery(@Valid @RequestBody ShippedToDeliveryRequest request);

    //Принять возврат товаров на склад
    @PostMapping("/return")
    @ResponseStatus(HttpStatus.OK)
    void returnProducts(@RequestBody Map<UUID, Long> products);
}
