package org.example.service;

import org.example.dto.CardPaymentRequestIssuer;
import org.example.exception.InvalidCardNumberException;
import org.example.exception.RequestExpiredException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class PccService {

    public void checkRequestValidity(CardPaymentRequestIssuer cardPaymentRequestIssuer) {
        if (!checkCreditCardNumber(cardPaymentRequestIssuer.getPan())) throw new InvalidCardNumberException("Invalid card number");
        Date current = new Date();
        long difference = current.getTime() - cardPaymentRequestIssuer.getAcquirerTimestamp().getTime();
        if (TimeUnit.MILLISECONDS.toMinutes(difference) > 10L) throw new RequestExpiredException("Request expired");
    }

    private boolean checkCreditCardNumber(String creditCardNumber) {
        ArrayList<String> results = new ArrayList<String>();
        int sumOfNumbersOnOddPosition = 0;
        for (int i = creditCardNumber.length() - 1; i >= 0; i--) {
            if (i % 2 != 0) {
                sumOfNumbersOnOddPosition += Character.getNumericValue(creditCardNumber.charAt(i));
                continue;
            }
            int a = Character.getNumericValue(creditCardNumber.charAt(i));
            int num = a * 2;
            results.add(String.valueOf(num));
        }
        int sumOfNumbersFromResult = 0;
        for(String res: results) {
            for (int i = 0; i < res.length(); i++) {
                sumOfNumbersFromResult += Character.getNumericValue(res.charAt(i));
            }
        }
        int checksum = sumOfNumbersFromResult + sumOfNumbersOnOddPosition;
        return checksum % 10 == 0;
    }
}
