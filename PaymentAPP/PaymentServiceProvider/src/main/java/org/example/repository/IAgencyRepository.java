package org.example.repository;

import org.example.model.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAgencyRepository extends JpaRepository<Agency, Long> {
}
