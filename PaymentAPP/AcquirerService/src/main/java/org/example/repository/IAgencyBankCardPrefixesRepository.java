package org.example.repository;

import org.example.model.AgencyBankCardPrefixes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAgencyBankCardPrefixesRepository extends JpaRepository<AgencyBankCardPrefixes, Long> {
    Optional<AgencyBankCardPrefixes> findAgencyBankCardPrefixesByPrefix(String prefix);
}
