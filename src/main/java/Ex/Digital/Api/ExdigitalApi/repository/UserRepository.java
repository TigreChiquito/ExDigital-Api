package Ex.Digital.Api.ExdigitalApi.repository;

import Ex.Digital.Api.ExdigitalApi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    List<User> findByIsActive(Boolean isActive);
    Boolean existsByEmail(String email);
}