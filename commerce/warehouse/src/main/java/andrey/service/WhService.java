package andrey.service;

import andrey.dto.shoppingcart.ShoppingCartDto;
import andrey.dto.warehouse.*;

import java.util.Map;
import java.util.UUID;

public interface WhService {

    void addProductToWh(NewProductInWarehouseRequest request);

    void increaseProductQuantity(AddProductToWarehouseRequest request);

    BookedProductsDto checkQuantityForCart(ShoppingCartDto cart);

    AddressDto getWhAddress();

    BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderRequest request);

    void sendToDelivery(ShippedToDeliveryRequest request);

    void returnProducts(Map<UUID, Long> products);
}
