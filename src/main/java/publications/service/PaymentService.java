package publications.service;

import java.util.List;
import java.util.Optional;

import publications.entity.Payment;
import publications.repository.PaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    public Payment pay(int sum) {
        Payment payment = new Payment(sum);
        paymentRepository.save(payment);

        return payment;
    }
}
