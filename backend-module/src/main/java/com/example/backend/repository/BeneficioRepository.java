package com.example.backend.repository;

import com.example.backend.entity.Beneficio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BeneficioRepository extends JpaRepository<Beneficio, Long> {
    
    @Query("SELECT b FROM Beneficio b WHERE b.ativo = true")
    List<Beneficio> findAllActive();
}