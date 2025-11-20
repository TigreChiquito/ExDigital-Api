package Ex.Digital.Api.ExdigitalApi.repository;

import Ex.Digital.Api.ExdigitalApi.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    List<Discount> findByActive(Boolean active);
    
    @Query("SELECT d FROM Discount d WHERE d.active = true AND d.initDate <= :currentDate AND d.endDate >= :currentDate")
    List<Discount> findActiveDiscountsByDate(LocalDate currentDate);
}