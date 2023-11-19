package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PccPaymentRequest extends CardPaymentRequest{
    private String acquirerId;
    private Date acquirerTimestamp;
    private float paymentPrice;

    public PccPaymentRequest(CardPaymentRequest cardPaymentRequest){
        super(cardPaymentRequest.getPaymentId(), cardPaymentRequest.getPan(), cardPaymentRequest.getSecurityCode(), cardPaymentRequest.getCardHolderName(), cardPaymentRequest.getExpirationDate());
    }
}
