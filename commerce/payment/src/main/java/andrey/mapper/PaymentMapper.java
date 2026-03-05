package andrey.mapper;

import andrey.dto.payment.PaymentDto;
import andrey.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    PaymentDto toDto(Payment payment);
}
