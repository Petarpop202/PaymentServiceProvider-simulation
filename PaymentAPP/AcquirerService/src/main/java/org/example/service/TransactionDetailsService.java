package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.TransactionDetails;
import org.example.exception.BadRequestException;
import org.example.exception.ErrorException;
import org.example.exception.NotFoundException;
import org.example.model.Agency;
import org.example.model.BankAccount;
import org.example.model.CreditCard;
import org.example.model.Payment;
import org.example.model.enums.PaymentStatus;
import org.example.repository.IAgencyRepository;
import org.example.repository.IBankAccountRepository;
import org.example.repository.ICreditCardRepository;
import org.example.repository.IPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class TransactionDetailsService {
    @Autowired
    private RestTemplate restTemplate;

    private final IAgencyRepository agencyRepository;

    private final IPaymentRepository paymentRepository;

    private final IBankAccountRepository bankAccountRepository;

    public void sendTransactionDetailsOnPsp(Payment payment) {
        TransactionDetails details = new TransactionDetails();
        details.setMerchantOrderId(payment.getMerchantOrderId());
        details.setPaymentId(payment.getId());
        details.setPaymentStatus(payment.getStatus());

        final String uri = "http://localhost:9003/api/payment/acquirer-bank-response";
        ResponseEntity<TransactionDetails> responseEntity = restTemplate.postForEntity(
                uri,
                details,
                TransactionDetails.class );
        // todo: Provera da li je psp dobio poruku u suprotnom prekinuti transakciju
        /*
        TransactionDetails responseDto = responseEntity.getBody();

        if(responseDto == null)
            throw new NotFoundException(message);
        */
    }

    public void errorPayment(Payment payment, String message) {
        payment.setStatus(PaymentStatus.ERROR);
        paymentRepository.save(payment);

        sendTransactionDetailsOnPsp(payment);
        throw new ErrorException(payment.getErrorUrl().toString());
    }

    public void failedPayment(Payment payment, String message) {
        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);

        sendTransactionDetailsOnPsp(payment);
        throw new ErrorException(payment.getFailedUrl().toString());
    }

    public void successPayment(Payment payment) {
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        sendTransactionDetailsOnPsp(payment);
    }

    public void differentBankSuccessPayment(Payment payment) {
        Agency agency = agencyRepository.findAgencyByMerchantId(payment.getMerchantId())
                .orElseThrow(() -> new NotFoundException("No seller account exist!"));;
        BankAccount bankAccount = agency.getBankAccount();
        bankAccount.setBalance(bankAccount.getBalance() + payment.getAmount());
        bankAccountRepository.save(bankAccount);
        successPayment(payment);
    }
}
