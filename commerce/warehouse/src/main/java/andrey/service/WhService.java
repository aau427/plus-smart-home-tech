package andrey.service;

import andrey.dto.shoppingcart.ShoppingCartDto;
import andrey.dto.warehouse.AddProductToWarehouseRequest;
import andrey.dto.warehouse.AddressDto;
import andrey.dto.warehouse.BookedProductsDto;
import andrey.dto.warehouse.NewProductInWarehouseRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

public interface WhService {

    void addProductToWh(NewProductInWarehouseRequest request);

    void increaseProductQuantity(AddProductToWarehouseRequest request);

    BookedProductsDto checkQuantityForCart(@Valid @RequestBody ShoppingCartDto cart);

    AddressDto getWhAddress();
}
