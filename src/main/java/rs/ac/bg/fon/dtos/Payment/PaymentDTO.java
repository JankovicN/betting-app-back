package rs.ac.bg.fon.dtos.Payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class PaymentDTO {
    private Integer userId;
    private BigDecimal amount;
}
