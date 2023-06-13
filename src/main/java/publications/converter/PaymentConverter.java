package publications.converter;

import publications.dto.PaymentDTO;
import publications.entity.Payment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentConverter {

    public PaymentDTO toDTO(Payment entity) {
        PaymentDTO dto = new PaymentDTO();

        dto.setId(entity.getId());
        dto.setSum(entity.getSum());

        return dto;
    }

    public Payment toEntity(PaymentDTO dto) {
        Payment entity = new Payment();

        entity.setId(dto.getId());
        entity.setSum(dto.getSum());

        return entity;
    }

    public List<PaymentDTO> listToDTO(List<Payment> paymentList) {
        List<PaymentDTO> result = new ArrayList<>();

        for (Payment payment : paymentList) {
            result.add(toDTO(payment));
        }

        return result;
    }
}
