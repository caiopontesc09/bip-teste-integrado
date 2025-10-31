package com.example.backend;

import com.example.backend.dto.BeneficioDto;
import com.example.backend.dto.TransferRequest;
import com.example.backend.entity.Beneficio;
import com.example.backend.service.BeneficioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/beneficios")
@CrossOrigin(origins = "*")
public class BeneficioController {

    @Autowired
    private BeneficioService beneficioService;

    @GetMapping
    public ResponseEntity<List<BeneficioDto>> list() {
        List<Beneficio> beneficios = beneficioService.findAll();
        List<BeneficioDto> dtos = beneficios.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeneficioDto> findById(@PathVariable Long id) {
        Optional<Beneficio> beneficio = beneficioService.findById(id);
        if (beneficio.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDto(beneficio.get()));
    }

    @PostMapping
    public ResponseEntity<BeneficioDto> create(@RequestBody BeneficioDto dto) {
        Beneficio beneficio = toEntity(dto);
        beneficio = beneficioService.save(beneficio);
        return ResponseEntity.ok(toDto(beneficio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BeneficioDto> update(@PathVariable Long id, @RequestBody BeneficioDto dto) {
        dto.setId(id);
        Beneficio beneficio = toEntity(dto);
        beneficio = beneficioService.save(beneficio);
        return ResponseEntity.ok(toDto(beneficio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        beneficioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request) {
        try {
            beneficioService.transfer(request.getFromId(), request.getToId(), request.getAmount());
            return ResponseEntity.ok("Transferência realizada com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro na transferência: " + e.getMessage());
        }
    }

    private BeneficioDto toDto(Beneficio entity) {
        return new BeneficioDto(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getValor(),
                entity.getAtivo(),
                entity.getVersion()
        );
    }

    private Beneficio toEntity(BeneficioDto dto) {
        Beneficio entity = new Beneficio();
        entity.setId(dto.getId());
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        entity.setValor(dto.getValor());
        entity.setAtivo(dto.getAtivo());
        entity.setVersion(dto.getVersion());
        return entity;
    }
}
