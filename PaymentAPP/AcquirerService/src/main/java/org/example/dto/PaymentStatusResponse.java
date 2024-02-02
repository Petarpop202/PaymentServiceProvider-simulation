package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enums.PaymentStatus;

import java.net.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatusResponse {
    private Long paymentId;
    private URL successUrl;
    private URL errorUrl;
    private URL failedUrl;
    private PaymentStatus status;
}
