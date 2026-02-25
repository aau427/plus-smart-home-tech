package andrey.controller;

import andrey.dto.warehouse.AddProductToWarehouseRequest;
import andrey.dto.warehouse.NewProductInWarehouseRequest;
import andrey.feignclient.WhAdminMethods;
import andrey.service.WhService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/warehouse")
@Validated
@RequiredArgsConstructor
public class AdminWhController implements WhAdminMethods {
    private final WhService service;


    //добавить новый товар на склад
    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addProductToWh(@Valid @RequestBody NewProductInWarehouseRequest request) {
        service.addProductToWh(request);
    }

    //Запрос на добавление определенного количества определенного товара.
    //т.е. товар уже в наличии, нужно увеличить его количество
    @Override
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void increaseProductQuantity(@Valid @RequestBody AddProductToWarehouseRequest request) {
        service.increaseProductQuantity(request);
    }
}
