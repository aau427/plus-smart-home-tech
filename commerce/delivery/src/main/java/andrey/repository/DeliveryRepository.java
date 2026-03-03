package andrey.repository;

import andrey.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
    boolean existsByOrderId(UUID orderId);

    Optional<Delivery> findByOrderId(UUID orderId);
}
