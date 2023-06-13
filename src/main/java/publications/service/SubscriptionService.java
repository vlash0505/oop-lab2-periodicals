package publications.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import publications.entity.Payment;
import publications.entity.Periodical;
import publications.entity.Subscription;
import publications.entity.User;
import publications.repository.PeriodicalRepository;
import publications.repository.SubscriptionRepository;

import publications.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubscriptionService {
    private final UserRepository userRepository;
    private final PeriodicalRepository periodicalRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentService paymentService;

    @Autowired
    public SubscriptionService(UserRepository userRepository, PeriodicalRepository periodicalRepository,
                               SubscriptionRepository subscriptionRepository, PaymentService paymentService) {
        this.userRepository = userRepository;
        this.periodicalRepository = periodicalRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.paymentService = paymentService;
    }

    public List<Subscription> findAll() {
        return subscriptionRepository.findAll();
    }

    public List<Subscription> findByUser(User user) {
        return subscriptionRepository.findByUser(user);
    }

    public List<Subscription> findByPeriodical(Periodical periodical) {
        return subscriptionRepository.findByPeriodical(periodical);
    }

    @Transactional
    public Subscription subscribe(long userId, long periodicalId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Periodical> periodical = periodicalRepository.findById(periodicalId);

        if (user.isPresent()) {
            if (periodical.isPresent()) {
                Payment payment = paymentService.pay(periodical.get().getPrice());
                Subscription subscription = new Subscription();

                subscription.setUser(user.get());
                subscription.setPeriodical(periodical.get());
                subscription.setPayment(payment);
                subscription.setExpirationDate(LocalDate.now().plusDays(periodical.get().getPeriod() * 10L));

                return subscription;
            }
        }

        return null;
    }
}
