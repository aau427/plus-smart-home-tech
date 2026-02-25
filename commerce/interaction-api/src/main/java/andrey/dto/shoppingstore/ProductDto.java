package andrey.dto.shoppingstore;


import andrey.enums.ProductCategory;
import andrey.enums.ProductState;
import andrey.enums.QuantityState;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private UUID productId;

    @NotBlank(message = "Наименование товара обязательно к заполнению!")
    private String productName;

    @NotBlank(message = "Описание товара обязательно к заполнению!")
    private String description;

    private String imageSrc;

    @NotNull(message = "Остаток обязателен к заполнению!")
    private QuantityState quantityState;

    @NotNull(message = "Статус товара обязателен к заполнению!")
    private ProductState productState;

    //в swagger не отмечено звездочкой, странно...
    private ProductCategory productCategory;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @DecimalMin(value = "1.0")
    private BigDecimal price;
}
