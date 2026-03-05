package andrey.model;

import andrey.enums.DeliveryState;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "deliveries")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "deliveryId")
public class Delivery {

    @Id
    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Column(name = "order_id")
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_state")
    private DeliveryState deliveryState;

    @Column(name = "country_from")
    private String countryFrom;

    @Column(name = "city_from")
    private String cityFrom;

    @Column(name = "street_from")
    private String streetFrom;

    @Column(name = "house_from")
    private String houseFrom;

    @Column(name = "flat_from")
    private String flatFrom;

    @Column(name = "country_to")
    private String countryTo;

    @Column(name = "city_to")
    private String cityTo;

    @Column(name = "street_to")
    private String streetTo;

    @Column(name = "house_to")
    private String houseTo;

    @Column(name = "flat_to")
    private String flatTo;
}
