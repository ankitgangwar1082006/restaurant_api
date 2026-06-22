package foodcourt.in.restaurant.repository;

import foodcourt.in.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    @Override
    Optional<Restaurant> findById(Long aLong);
}
