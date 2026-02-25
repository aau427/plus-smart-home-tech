package andrey.repository;

import andrey.enums.ProductCategory;
import andrey.enums.ProductState;
import andrey.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Page<Product> findAllByProductCategoryAndProductState(ProductCategory category,
                                                          ProductState state,
                                                          Pageable pageable);
}
