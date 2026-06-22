package foodcourt.in.restaurant.repository;

import foodcourt.in.restaurant.entity.User;
import foodcourt.in.restaurant.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {
}
