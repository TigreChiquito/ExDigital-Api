package Ex.Digital.Api.ExdigitalApi.repository;

import Ex.Digital.Api.ExdigitalApi.entity.Order;
import Ex.Digital.Api.ExdigitalApi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUser(User user);
    List<Order> findByUserIdUser(Integer userId);
    List<Order> findByStatus(String status);
    Optional<Order> findByOrderNumber(String orderNumber);
    
    @Query("SELECT o FROM Order o WHERE o.user.idUser = :userId ORDER BY o.createdAt DESC")
    List<Order> findByUserOrderByCreatedAtDesc(Integer userId);
}