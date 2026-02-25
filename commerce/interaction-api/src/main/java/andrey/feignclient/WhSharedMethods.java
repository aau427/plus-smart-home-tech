package andrey.feignclient;

import andrey.dto.shoppingcart.ShoppingCartDto;
import andrey.dto.warehouse.AddressDto;
import andrey.dto.warehouse.BookedProductsDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "warehouse", contextId = "warehouseShared", path = "/api/v1/warehouse")
public interface WhSharedMethods {
    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    BookedProductsDto checkQuantityForCart(@Valid @RequestBody ShoppingCartDto cart);


    @GetMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    AddressDto getWhAddress();
}
