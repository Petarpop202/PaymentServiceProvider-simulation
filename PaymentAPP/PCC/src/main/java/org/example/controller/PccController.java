package org.example.controller;

import com.google.gson.Gson;
import org.example.dto.CardPaymentRequestIssuer;
import org.example.dto.SuccessfulPaymentData;
import org.example.service.PccService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/api/pcc")
public class PccController {

    @Autowired
    private PccService pccService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping(value = "/different-bank")
    public ResponseEntity<SuccessfulPaymentData> differentIssuerBank(@RequestBody CardPaymentRequestIssuer cardPaymentRequestIssuer) {
        pccService.checkRequestValidity(cardPaymentRequestIssuer);
        return restTemplate.postForEntity("https://localhost:9010/api/issuer/card-payment", cardPaymentRequestIssuer, SuccessfulPaymentData.class);
    }
}
