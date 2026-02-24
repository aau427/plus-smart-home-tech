package andrey.service;

import andrey.dto.shoppingcart.ChangeProductQuantityRequest;
import andrey.dto.shoppingcart.ShoppingCartDto;
import andrey.enums.ShoppingCartState;
import andrey.exception.cart.CartNotAllowedForModification;
import andrey.exception.cart.NoProductsInShoppingCartException;
import andrey.feignclient.WhSharedMethods;
import andrey.mapper.CartMapper;
import andrey.model.ShoppingCart;
import andrey.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository repository;
    private final CartMapper mapper;
    private final WhSharedMethods whClient;

    @Transactional
    @Override
    public ShoppingCartDto addProductsToCart(String username, Map<UUID, Integer> newProducts) {
        ShoppingCart cart = createOrGetIfExistsCart(username);
        validateCart(cart);
        if (newProducts != null) {
            newProducts.forEach(
                    (productId, quantity) -> cart.getProducts().merge(productId, quantity,
                            (existsQuantity, newQuantity) -> existsQuantity + newQuantity)
            );
        }
        ShoppingCartDto cartDto = mapper.toDto(cart);
        whClient.checkQuantityForCart(cartDto);
        ShoppingCart newCart = repository.save(cart);
        log.debug("В корзину для пользователя {} добавили продукты. Корзина после изменения  {} шт позиций товаров", newCart.getUsername(), newCart.getProducts().size());
        return mapper.toDto(cart);
    }

    @Transactional
    @Override
    public ShoppingCartDto removeProductsFromCart(String username, Set<UUID> products) {
        ShoppingCart cart = getOrThrowNotFoundException(username);
        validateCart(cart);
        checkProductsExistInCart(cart, products);
        products.forEach(productId -> cart.getProducts().remove(productId));
        ShoppingCart newCart = repository.save(cart);
        log.debug("Из корзины пользователя {} удалили продукты. Корзина после изменения: {} шт позиций товаров", cart.getUsername(), newCart.getProducts().size());
        return mapper.toDto(newCart);
    }

    @Override
    public ShoppingCartDto getCart(String username) {
        /*
            согласно Swagger: новая или ранее созданная
         */
        ShoppingCart cart = createOrGetIfExistsCart(username);
        return mapper.toDto(cart);
    }

    @Override
    public void deactivateCart(String username) {

        final ShoppingCart cart = getOrThrowNotFoundException(username);

        if (cart.getCartState() == ShoppingCartState.DEACTIVATED) {
            log.debug("Корзина уже деактивирована!");
            return;
        }

        cart.setCartState(ShoppingCartState.DEACTIVATED);
        repository.save(cart);
        log.debug("Корзина id = {} удалена для пользователя {} ",
                cart.getCartId(), cart.getUsername());
    }

    @Override
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        ShoppingCart cart = getOrThrowNotFoundException(username);
        validateCart(cart);
        UUID productId = request.getProductId();
        if (!cart.getProducts().containsKey(productId)) {
            throw new NoProductsInShoppingCartException(
                    String.format("Товар с ID %s не найден в вашей корзине", productId)
            );
        }

        Integer newQuantity = request.getNewQuantity();
        cart.getProducts().put(productId, newQuantity);
        ShoppingCart newCart = repository.save(cart);
        log.debug("Изменили корзину ддя пользователя {}. Теперь количества товара Id = {}  установлено в {}", newCart.getUsername(), productId, newQuantity);
        return mapper.toDto(newCart);

    }

    private ShoppingCart createOrGetIfExistsCart(String username) {
        return repository.findByUsername(username)
                .orElseGet(
                        () -> createCart(username)
                );
    }

    private ShoppingCart getOrThrowNotFoundException(String username) {
        return repository.findByUsername(username).orElseThrow(
                () -> new NoProductsInShoppingCartException("Не найдена корзина для пользователя: " + username)
        );
    }

    private ShoppingCart createCart(final String username) {
        ShoppingCart cart = new ShoppingCart();
        cart.setCartId(UUID.randomUUID());
        cart.setUsername(username);
        cart.setCartState(ShoppingCartState.ACTIVE);
        cart.setProducts(new HashMap<>());

        ShoppingCart createdCart = repository.save(cart);

        log.debug("Создал корзину {} для пользователя {}", createdCart.getCartId(), createdCart.getUsername());
        return createdCart;
    }

    private void checkProductsExistInCart(ShoppingCart cart, Set<UUID> products) {
        /*
            Согласно Swagger проверяем на условие "Нет искомых товаров в корзине"
            что значит это условие????? а если хотя бы один удаляемый есть в корзине?????
            в общем читаю буквально - ВСЕ удаляемые товары должны быть в корзине, иначе ошибка
            кстати
         */
        if (!cart.getProducts().keySet().containsAll(products)) {
            throw new NoProductsInShoppingCartException(
                    "Все удаляемые товары должны быть в корзине!");
        }
    }

    private void validateCart(ShoppingCart cart) {
        if (cart.getCartState() == ShoppingCartState.DEACTIVATED) {
            throw new CartNotAllowedForModification(cart.getCartId());
        }
    }
}
