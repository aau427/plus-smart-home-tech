package andrey.mapper;

import andrey.dto.warehouse.AddressDto;
import andrey.dto.warehouse.NewProductInWarehouseRequest;
import andrey.model.Dimension;
import andrey.model.Product;
import andrey.service.AddressService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WhMapper {

    @Mapping(target = "quantity", constant = "0L") // Начальное количество при регистрации
    @Mapping(target = "dimension", source = "dimension")
    Product toEntity(NewProductInWarehouseRequest request);

    AddressDto toDto(AddressService address);

    Dimension mapDimension(andrey.dto.warehouse.DimensionDto dto);
}