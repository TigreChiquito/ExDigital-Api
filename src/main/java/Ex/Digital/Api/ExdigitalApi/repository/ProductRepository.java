package Ex.Digital.Api.ExdigitalApi.repository;

import Ex.Digital.Api.ExdigitalApi.entity.Product;
import Ex.Digital.Api.ExdigitalApi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByIsActive(Boolean isActive);
    List<Product> findByCategory(Category category);
    List<Product> findByCategoryIdCategories(Integer categoryId);
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.stock > 0")
    List<Product> findAvailableProducts();
    
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) AND p.isActive = true")
    List<Product> searchByName(@Param("keyword") String keyword);

    // --- NUEVO: Consulta optimizada para evitar el problema N+1 ---
    @Query("SELECT DISTINCT p FROM Product p " +
           "LEFT JOIN FETCH p.images " +
           "LEFT JOIN FETCH p.category " +
           "LEFT JOIN FETCH p.discount")
    List<Product> findAllWithRelations();
}