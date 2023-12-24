package org.example.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentMethod {

    private long id;

    private String name;

    public PaymentMethod(org.example.model.PaymentMethod paymentMethod) {
        id = paymentMethod.getId();
        name = paymentMethod.getName();
    }
}
