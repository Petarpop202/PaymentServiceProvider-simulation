package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoPaymentData {

    @JsonProperty(value = "price_amount")
    private double amount;

    @JsonProperty(value = "price_currency")
    private String currency;

    @JsonProperty(value = "receive_currency")
    private String receiveCurrency;

    private String title;

    @JsonProperty(value = "success_url")
    private String successUrl;

    @JsonProperty(value = "callback_url")
    private String callbackUrl;
}
