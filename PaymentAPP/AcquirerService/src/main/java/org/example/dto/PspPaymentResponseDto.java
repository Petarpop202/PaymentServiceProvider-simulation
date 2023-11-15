package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PspPaymentResponseDto {
    private Long paymentId;
    private URL paymentUrl;
}
