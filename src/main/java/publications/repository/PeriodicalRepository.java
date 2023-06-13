package publications.repository;

import java.util.List;

import publications.entity.Periodical;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PeriodicalRepository extends JpaRepository<Periodical, Long> {

    List<Periodical> findByName(String name);
}
