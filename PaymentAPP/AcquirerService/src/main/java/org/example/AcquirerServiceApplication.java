package org.example;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@SpringBootApplication()
public class AcquirerServiceApplication {
    public static void main(String[] args) { SpringApplication.run(AcquirerServiceApplication.class, args);}
    @Value("${http.client.ssl.trust-store}")
    private Resource keyStoreFile;
    @Value("${http.client.ssl.trust-store-password}")
    private String keyStorePassword;
    @Bean
    public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream(keyStoreFile.getFile()),
                keyStorePassword.toCharArray());

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                new SSLContextBuilder()
                        .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                        .loadKeyMaterial(keyStore, keyStorePassword.toCharArray())
                        .build(),
                NoopHostnameVerifier.INSTANCE);

        HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(
                socketFactory).build();

        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
                httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        return restTemplate;
    }
}