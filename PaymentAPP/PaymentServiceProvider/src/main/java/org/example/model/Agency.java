package org.example.model;
import lombok.*;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "agencies")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Agency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name= "merchant_id")
    private String merchantId;

    @Column(name = "merchant_password")
    private String merchantPassword;
}
