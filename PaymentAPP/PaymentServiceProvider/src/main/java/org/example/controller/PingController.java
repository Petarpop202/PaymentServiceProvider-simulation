package org.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class PingController {
    @Autowired
    private RestTemplate restTemplate;
    @GetMapping("/crypto")
    public ResponseEntity<String> pingCryptoService() throws Exception {
        final String uri = "https://localhost:9001/api/ping";
        return pingMethod(uri);
    }

    @GetMapping("/payPal")
    public ResponseEntity<String> pingPayPalService() throws Exception {
        final String uri = "https://localhost:9004/api/ping";
        return pingMethod(uri);
    }

    @GetMapping("/ips")
    public ResponseEntity<String> pingIPSService() throws Exception {
        final String uri = "https://localhost:9006/api/ping";
        return pingMethod(uri);
    }

    private ResponseEntity<String> pingMethod(String uri){
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {
                });
        String s = responseEntity.getBody();
        if (s != null) {
            return new ResponseEntity<>(s, HttpStatus.OK);
        }
        return new ResponseEntity<>("Error",HttpStatus.NOT_FOUND);
    }
}
