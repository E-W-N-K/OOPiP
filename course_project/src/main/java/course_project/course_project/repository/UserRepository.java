package course_project.course_project.repository;

import course_project.course_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String username);
    boolean existsByName(String username);
    boolean existsByEmail(String email);
}
