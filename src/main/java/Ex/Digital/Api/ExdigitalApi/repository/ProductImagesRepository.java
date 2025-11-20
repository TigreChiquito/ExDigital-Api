package Ex.Digital.Api.ExdigitalApi.repository;

import Ex.Digital.Api.ExdigitalApi.entity.ProductImages;
import Ex.Digital.Api.ExdigitalApi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImagesRepository extends JpaRepository<ProductImages, Integer> {
    List<ProductImages> findByProductOrderByOrderPositionAsc(Product product);
    List<ProductImages> findByProductIdProductOrderByOrderPositionAsc(Integer productId);
    Optional<ProductImages> findByProductAndIsPrimaryTrue(Product product);
}