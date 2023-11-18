package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardPaymentRequestIssuer {

    private String paymentId;

    private float paymentPrice;

    private String acquirerId;

    private Date acquirerTimestamp;

    private String pan;

    private String securityCode;

    private String cardHolderName;

    private Date expirationDate;


}
