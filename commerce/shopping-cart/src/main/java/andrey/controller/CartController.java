package andrey.controller;

import andrey.dto.shoppingcart.ChangeProductQuantityRequest;
import andrey.dto.shoppingcart.ShoppingCartDto;
import andrey.feignclient.CartMethods;
import andrey.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
@Validated
public class CartController implements CartMethods {

    private final CartService service;

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto getCart(
            @Valid @RequestParam @NotBlank String username) {
        return service.getCart(username);
    }

    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto addProductToCart(
            @Valid @NotEmpty @RequestBody Map<@NotNull UUID, @NotNull @Positive Integer> products,
            @Valid @RequestParam @NotBlank String username) {
        return service.addProductsToCart(username, products);
    }

    @Override
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deactivateCurrentCart(
            @Valid @RequestParam @NotBlank String username) {
        service.deactivateCart(username);
    }

    @Override
    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto removeProductsFromCart(
            @Valid @RequestParam @NotBlank String username,
            @Valid @NotEmpty @RequestBody Set<@NotNull UUID> products) {
        return service.removeProductsFromCart(username, products);
    }

    @Override
    @PostMapping("/change-quantity")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto changeQuantity(
            @Valid @RequestParam @NotBlank String username,
            @Valid @RequestBody ChangeProductQuantityRequest request) {
        return service.changeProductQuantity(username, request);
    }
}
