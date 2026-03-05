package andrey.mapper;

import andrey.dto.delivery.DeliveryDto;
import andrey.enums.DeliveryState;
import andrey.model.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        imports = {UUID.class, DeliveryState.class},
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE
)
public interface DeliveryMapper {
    @Mapping(target = "deliveryId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "deliveryState", expression = "java(DeliveryState.CREATED)")
    @Mapping(target = "countryFrom", source = "fromAddress.country")
    @Mapping(target = "cityFrom", source = "fromAddress.city")
    @Mapping(target = "streetFrom", source = "fromAddress.street")
    @Mapping(target = "houseFrom", source = "fromAddress.house")
    @Mapping(target = "flatFrom", source = "fromAddress.flat")
    @Mapping(target = "countryTo", source = "toAddress.country")
    @Mapping(target = "cityTo", source = "toAddress.city")
    @Mapping(target = "streetTo", source = "toAddress.street")
    @Mapping(target = "houseTo", source = "toAddress.house")
    @Mapping(target = "flatTo", source = "toAddress.flat")
    Delivery toEntity(DeliveryDto dto);

    @Mapping(target = "fromAddress.country", source = "countryFrom")
    @Mapping(target = "fromAddress.city", source = "cityFrom")
    @Mapping(target = "fromAddress.street", source = "streetFrom")
    @Mapping(target = "fromAddress.house", source = "houseFrom")
    @Mapping(target = "fromAddress.flat", source = "flatFrom")
    @Mapping(target = "toAddress.country", source = "countryTo")
    @Mapping(target = "toAddress.city", source = "cityTo")
    @Mapping(target = "toAddress.street", source = "streetTo")
    @Mapping(target = "toAddress.house", source = "houseTo")
    @Mapping(target = "toAddress.flat", source = "flatTo")
    DeliveryDto toDto(Delivery entity);
}
