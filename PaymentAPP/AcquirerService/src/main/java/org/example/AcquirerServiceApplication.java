package org.example;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class AcquirerServiceApplication {
    public static void main(String[] args) { SpringApplication.run(AcquirerServiceApplication.class, args);}
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}