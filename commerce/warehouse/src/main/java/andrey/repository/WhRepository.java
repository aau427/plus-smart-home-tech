package andrey.repository;

import andrey.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WhRepository extends JpaRepository<Product, UUID> {

}
