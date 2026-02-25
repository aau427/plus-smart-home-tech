package andrey.feignclient;

import andrey.dto.warehouse.AddProductToWarehouseRequest;
import andrey.dto.warehouse.NewProductInWarehouseRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "warehouse", contextId = "warehouseAdmin", path = "/api/v1/warehouse")
public interface WhAdminMethods {


    //добавить новый товар на склад
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    void addProductToWh(@Valid @RequestBody NewProductInWarehouseRequest request);

    //Запрос на добавление определенного количества определенного товара.
    //т.е. товар уже в наличии, нужно увеличить его количество
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    void increaseProductQuantity(@Valid @RequestBody AddProductToWarehouseRequest request);
}
