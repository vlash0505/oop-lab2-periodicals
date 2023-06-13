package publications.repository;

import java.util.List;

import publications.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByUsername(String login);
}
