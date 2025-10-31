package com.example.backend.service;

import com.example.backend.entity.Beneficio;
import com.example.backend.repository.BeneficioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeneficioServiceTest {

    @Mock
    private BeneficioRepository repository;

    @InjectMocks
    private BeneficioService service;

    private Beneficio beneficio1;
    private Beneficio beneficio2;

    @BeforeEach
    void setUp() {
        beneficio1 = new Beneficio("Beneficio 1", "Descrição 1", new BigDecimal("100.00"), true);
        beneficio1.setId(1L);
        
        beneficio2 = new Beneficio("Beneficio 2", "Descrição 2", new BigDecimal("50.00"), true);
        beneficio2.setId(2L);
    }

    @Test
    void deveRetornarTodosBeneficiosAtivos() {
        when(repository.findAllActive()).thenReturn(Arrays.asList(beneficio1, beneficio2));

        List<Beneficio> result = service.findAll();

        assertEquals(2, result.size());
        verify(repository).findAllActive();
    }

    @Test
    void deveRetornarBeneficio_QuandoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(beneficio1));

        Optional<Beneficio> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(beneficio1, result.get());
        verify(repository).findById(1L);
    }

    @Test
    void deveRetornarVazio_QuandoNaoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Optional<Beneficio> result = service.findById(1L);

        assertFalse(result.isPresent());
        verify(repository).findById(1L);
    }

    @Test
    void deveRetornarBeneficioSalvo() {
        when(repository.save(beneficio1)).thenReturn(beneficio1);

        Beneficio result = service.save(beneficio1);

        assertEquals(beneficio1, result);
        verify(repository).save(beneficio1);
    }

    @Test
    void deveDefinirAtivoComoFalso_QuandoBeneficioExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(beneficio1));
        when(repository.save(any(Beneficio.class))).thenReturn(beneficio1);

        service.delete(1L);

        assertFalse(beneficio1.getAtivo());
        verify(repository).findById(1L);
        verify(repository).save(beneficio1);
    }

    @Test
    void naoDeveFazerNada_QuandoBeneficioNaoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        service.delete(1L);

        verify(repository).findById(1L);
        verify(repository, never()).save(any());
    }

    @Test
    void deveTransferirValor_QuandoParametrosValidos() {
        when(repository.findById(1L)).thenReturn(Optional.of(beneficio1));
        when(repository.findById(2L)).thenReturn(Optional.of(beneficio2));

        service.transfer(1L, 2L, new BigDecimal("30.00"));

        assertEquals(new BigDecimal("70.00"), beneficio1.getValor());
        assertEquals(new BigDecimal("80.00"), beneficio2.getValor());
        verify(repository).save(beneficio1);
        verify(repository).save(beneficio2);
    }

    @Test
    void deveLancarExcecao_QuandoIdOrigemNulo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.transfer(null, 2L, new BigDecimal("30.00")));

        assertEquals("IDs e valor não podem ser nulos", exception.getMessage());
    }

    @Test
    void deveLancarExcecao_QuandoIdDestinoNulo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.transfer(1L, null, new BigDecimal("30.00")));

        assertEquals("IDs e valor não podem ser nulos", exception.getMessage());
    }

    @Test
    void deveLancarExcecao_QuandoValorNulo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.transfer(1L, 2L, null));

        assertEquals("IDs e valor não podem ser nulos", exception.getMessage());
    }

    @Test
    void deveLancarExcecao_QuandoValorZero() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.transfer(1L, 2L, BigDecimal.ZERO));

        assertEquals("Valor deve ser positivo", exception.getMessage());
    }

    @Test
    void deveLancarExcecao_QuandoValorNegativo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.transfer(1L, 2L, new BigDecimal("-10.00")));

        assertEquals("Valor deve ser positivo", exception.getMessage());
    }

    @Test
    void deveLancarExcecao_QuandoMesmoId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.transfer(1L, 1L, new BigDecimal("30.00")));

        assertEquals("Não é possível transferir para o mesmo benefício", exception.getMessage());
    }

    @Test
    void deveLancarExcecao_QuandoBeneficioOrigemNaoEncontrado() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.transfer(1L, 2L, new BigDecimal("30.00")));

        assertEquals("Benefício origem não encontrado: 1", exception.getMessage());
    }

    @Test
    void deveLancarExcecao_QuandoBeneficioDestinoNaoEncontrado() {
        when(repository.findById(1L)).thenReturn(Optional.of(beneficio1));
        when(repository.findById(2L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.transfer(1L, 2L, new BigDecimal("30.00")));

        assertEquals("Benefício destino não encontrado: 2", exception.getMessage());
    }

    @Test
    void deveLancarExcecao_QuandoBeneficioOrigemInativo() {
        beneficio1.setAtivo(false);
        when(repository.findById(1L)).thenReturn(Optional.of(beneficio1));
        when(repository.findById(2L)).thenReturn(Optional.of(beneficio2));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.transfer(1L, 2L, new BigDecimal("30.00")));

        assertEquals("Benefício origem está inativo", exception.getMessage());
    }

    @Test
    void deveLancarExcecao_QuandoBeneficioDestinoInativo() {
        beneficio2.setAtivo(false);
        when(repository.findById(1L)).thenReturn(Optional.of(beneficio1));
        when(repository.findById(2L)).thenReturn(Optional.of(beneficio2));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.transfer(1L, 2L, new BigDecimal("30.00")));

        assertEquals("Benefício destino está inativo", exception.getMessage());
    }

    @Test
    void deveLancarExcecao_QuandoSaldoInsuficiente() {
        when(repository.findById(1L)).thenReturn(Optional.of(beneficio1));
        when(repository.findById(2L)).thenReturn(Optional.of(beneficio2));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.transfer(1L, 2L, new BigDecimal("150.00")));

        assertEquals("Saldo insuficiente. Saldo atual: 100.00, Valor solicitado: 150.00", exception.getMessage());
    }
}