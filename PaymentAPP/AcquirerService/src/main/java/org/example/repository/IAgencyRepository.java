package org.example.repository;

import org.example.model.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IAgencyRepository extends JpaRepository<Agency, Long> {

    Optional<Agency> findAgencyByMerchantId(String merchantId);
}
