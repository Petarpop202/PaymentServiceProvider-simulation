package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessfulPaymentData {

    private long issuerOrderId;
    private Date issuerOrderTimestamp;
    @Setter
    private PaymentStatus status;
}
