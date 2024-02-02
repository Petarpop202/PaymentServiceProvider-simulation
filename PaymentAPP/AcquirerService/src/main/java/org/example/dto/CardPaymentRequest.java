package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardPaymentRequest {
    private Long paymentId;
    private String pan;
    private String securityCode;
    private String cardHolderName;
    private Date expirationDate;
}
