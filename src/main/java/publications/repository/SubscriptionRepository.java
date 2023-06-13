package publications.repository;

import java.util.List;

import publications.entity.Periodical;
import publications.entity.Subscription;
import publications.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUser(User user);

    List<Subscription> findByPeriodical(Periodical periodical);
}
