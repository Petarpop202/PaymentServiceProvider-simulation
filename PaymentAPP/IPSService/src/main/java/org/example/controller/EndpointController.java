package org.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EndpointController {
    @GetMapping("/ping")
    public ResponseEntity<String> getPing() {
        String s = "Success IPS connection!";
        return new ResponseEntity<>(s, HttpStatus.OK);
    }
}
