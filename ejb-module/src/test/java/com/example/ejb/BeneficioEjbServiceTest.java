package com.example.ejb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BeneficioEjbServiceTest {

    @Mock
    private EntityManager entityManager;

    private BeneficioEjbService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new BeneficioEjbService();
        try {
            var field = BeneficioEjbService.class.getDeclaredField("em");
            field.setAccessible(true);
            field.set(service, entityManager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testTransferSuccess() {
        Beneficio from = new Beneficio("Origem", "Desc", new BigDecimal("1000"), true);
        from.setId(1L);
        Beneficio to = new Beneficio("Destino", "Desc", new BigDecimal("500"), true);
        to.setId(2L);

        when(entityManager.find(Beneficio.class, 1L, LockModeType.OPTIMISTIC)).thenReturn(from);
        when(entityManager.find(Beneficio.class, 2L, LockModeType.OPTIMISTIC)).thenReturn(to);

        service.transfer(1L, 2L, new BigDecimal("200"));

        assertEquals(new BigDecimal("800"), from.getValor());
        assertEquals(new BigDecimal("700"), to.getValor());
        verify(entityManager).merge(from);
        verify(entityManager).merge(to);
    }

    @Test
    void testTransferInsufficientFunds() {
        Beneficio from = new Beneficio("Origem", "Desc", new BigDecimal("100"), true);
        from.setId(1L);
        Beneficio to = new Beneficio("Destino", "Desc", new BigDecimal("500"), true);
        to.setId(2L);

        when(entityManager.find(Beneficio.class, 1L, LockModeType.OPTIMISTIC)).thenReturn(from);
        when(entityManager.find(Beneficio.class, 2L, LockModeType.OPTIMISTIC)).thenReturn(to);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.transfer(1L, 2L, new BigDecimal("200"))
        );
        
        assertTrue(exception.getMessage().contains("Saldo insuficiente"));
        verify(entityManager, never()).merge(any());
    }

    @Test
    void testTransferNullAmount() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.transfer(1L, 2L, null)
        );
        
        assertEquals("IDs e valor não podem ser nulos", exception.getMessage());
    }

    @Test
    void testTransferSameId() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.transfer(1L, 1L, new BigDecimal("100"))
        );
        
        assertEquals("Não é possível transferir para o mesmo benefício", exception.getMessage());
    }
}