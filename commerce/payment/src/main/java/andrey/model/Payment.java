package andrey.model;

import andrey.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "paymentId")
@Builder
@AllArgsConstructor
public class Payment {

    @Id
    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "delivery_total")
    private BigDecimal deliveryTotal;

    @Column(name = "total_payment")
    private BigDecimal totalPayment;

    @Column(name = "fee_total")
    private BigDecimal feeTotal;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
