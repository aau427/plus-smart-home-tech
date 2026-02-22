package andrey.service;

import andrey.dto.shoppingcart.ChangeProductQuantityRequest;
import andrey.dto.shoppingcart.ShoppingCartDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface CartService {

    ShoppingCartDto addProductsToCart(String username, Map<UUID, Integer> products);

    ShoppingCartDto removeProductsFromCart(String username, Set<UUID> products);

    ShoppingCartDto getCart(String username);

    void deactivateCart(String username);

    ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request);
}
