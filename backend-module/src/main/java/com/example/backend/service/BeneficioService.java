package com.example.backend.service;

import com.example.backend.entity.Beneficio;
import com.example.backend.repository.BeneficioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BeneficioService {

    @Autowired
    private BeneficioRepository repository;

    public List<Beneficio> findAll() {
        return repository.findAllActive();
    }

    public Optional<Beneficio> findById(Long id) {
        return repository.findById(id);
    }

    public Beneficio save(Beneficio beneficio) {
        return repository.save(beneficio);
    }

    public void delete(Long id) {
        Optional<Beneficio> beneficio = repository.findById(id);
        if (beneficio.isPresent()) {
            beneficio.get().setAtivo(false);
            repository.save(beneficio.get());
        }
    }

    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        if (fromId == null || toId == null || amount == null) {
            throw new IllegalArgumentException("IDs e valor não podem ser nulos");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Não é possível transferir para o mesmo benefício");
        }

        Optional<Beneficio> fromOpt = repository.findById(fromId);
        Optional<Beneficio> toOpt = repository.findById(toId);

        if (fromOpt.isEmpty()) {
            throw new IllegalArgumentException("Benefício origem não encontrado: " + fromId);
        }
        if (toOpt.isEmpty()) {
            throw new IllegalArgumentException("Benefício destino não encontrado: " + toId);
        }

        Beneficio from = fromOpt.get();
        Beneficio to = toOpt.get();

        if (!from.getAtivo()) {
            throw new IllegalArgumentException("Benefício origem está inativo");
        }
        if (!to.getAtivo()) {
            throw new IllegalArgumentException("Benefício destino está inativo");
        }

        if (from.getValor().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente. Saldo atual: " + from.getValor() + ", Valor solicitado: " + amount);
        }

        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));

        repository.save(from);
        repository.save(to);
    }
}