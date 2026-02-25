package andrey.model;

import andrey.enums.ShoppingCartState;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "shopping_carts", schema = "cart")
@Getter
@Setter
@EqualsAndHashCode(of = "cartId")
@NoArgsConstructor
public class ShoppingCart {

    @Id
    @Column(name = "cart_id", updatable = false, nullable = false)
    private UUID cartId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "cart_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShoppingCartState cartState;

    @ElementCollection
    @CollectionTable(name = "shopping_cart_products", schema = "cart",
            joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Integer> products = new HashMap<>();
}