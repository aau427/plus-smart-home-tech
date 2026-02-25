package andrey.feignclient;

import andrey.dto.shoppingcart.ChangeProductQuantityRequest;
import andrey.dto.shoppingcart.ShoppingCartDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface CartMethods {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ShoppingCartDto getCart(
            @Valid @RequestParam @NotBlank String username);

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    ShoppingCartDto addProductToCart(
            @Valid @NotEmpty @RequestBody Map<@NotNull UUID, @NotNull @Positive Integer> products,
            @Valid @RequestParam @NotBlank String username);

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    void deactivateCurrentCart(
            @Valid @RequestParam @NotBlank String username);

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    ShoppingCartDto removeProductsFromCart(
            @Valid @RequestParam @NotBlank String username,
            @Valid @NotEmpty @RequestBody Set<@NotNull UUID> products);

    @PostMapping("/change-quantity")
    @ResponseStatus(HttpStatus.OK)
    ShoppingCartDto changeQuantity(
            @Valid @RequestParam @NotBlank String username,
            @Valid @RequestBody ChangeProductQuantityRequest request);
}
