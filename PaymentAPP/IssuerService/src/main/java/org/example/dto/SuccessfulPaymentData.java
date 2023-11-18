package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessfulPaymentData {

    private long issuerOrderId;
    private Date issuerOrderTimestamp;
}
