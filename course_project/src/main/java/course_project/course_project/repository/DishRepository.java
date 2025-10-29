package course_project.course_project.repository;

import course_project.course_project.model.Dish;
import course_project.course_project.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {
    @Query( "SELECT d " +
            "FROM Dish d " +
            "WHERE d.name LIKE %?1%")
    List<Dish> search(String name);
}
