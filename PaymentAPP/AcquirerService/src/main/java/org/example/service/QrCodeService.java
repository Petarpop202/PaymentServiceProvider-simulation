package org.example.service;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.RequiredArgsConstructor;
import org.example.dto.QrCodeGeneratorRequest;
import org.example.dto.QrCodePaymentRequest;
import org.example.dto.SuccessfulPaymentData;
import org.example.model.Agency;
import org.example.model.BankAccount;
import org.example.model.Payment;
import org.example.repository.IAgencyRepository;
import org.example.repository.IBankAccountRepository;
import org.example.repository.IPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class QrCodeService {

    private final IPaymentRepository paymentRepository;

    private final IBankAccountRepository bankAccountRepository;

    private final IAgencyRepository agencyRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String QR_CODE_GEN_URL = "https://nbs.rs/QRcode/api/qr/v1/gen/500";

    private static final String QR_CODE_VAL_URL = "https://nbs.rs/QRcode/api/qr/v1/validate";

    private Logger logger = LoggerFactory.getLogger(QrCodeService.class);

    public byte[] generateQrCode(QrCodePaymentRequest qrCodePaymentRequest) {
        Payment payment = paymentRepository.findById(qrCodePaymentRequest.getPaymentId()).orElseThrow(null);
        QrCodeGeneratorRequest qrCodeGeneratorRequest = createRequest(payment);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<QrCodeGeneratorRequest> requestEntity = new HttpEntity<>(qrCodeGeneratorRequest, headers);

        ResponseEntity<byte[]> responseEntity = restTemplate.postForEntity(
                QR_CODE_GEN_URL,
                requestEntity,
                byte[].class);
        logger.info("User requested ips code payment !");
        return responseEntity.getBody();
    }

    private QrCodeGeneratorRequest createRequest(Payment payment){
        QrCodeGeneratorRequest qrCodeGeneratorRequest = new QrCodeGeneratorRequest();
        Agency agency = agencyRepository.findAgencyByMerchantId(payment.getMerchantId()).orElseThrow(null);
        BankAccount bankAccount = agency.getBankAccount();
        qrCodeGeneratorRequest.setR(bankAccount.getAccountNumber());
        qrCodeGeneratorRequest.setP("Predrag Popovic");

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        qrCodeGeneratorRequest.setI("RSD" + decimalFormat.format(payment.getAmount()));

        qrCodeGeneratorRequest.setN("Agencija");
        return qrCodeGeneratorRequest;
    }


}
