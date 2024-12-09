package org.rogs.app.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import org.rogs.app.dto.CartaoRequestDTO;
import org.rogs.app.model.Cartao;
import org.rogs.app.repository.CartaoRepository;
import org.rogs.app.service.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CartaoServiceTest {

    @MockBean
    private CartaoRepository cartaoRepository;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private CartaoService cartaoService;

    @Test
    public void testCriarCartao_OK() {
        CartaoRequestDTO requestDTO = new CartaoRequestDTO();
        Cartao cartao = new Cartao();
        when(modelMapper.map(any(),any())).thenReturn(cartao);
        when(cartaoRepository.save(any())).thenReturn(cartao);

        assertDoesNotThrow(() -> cartaoService.criar(requestDTO));
        verify(cartaoRepository, times(1)).save(cartao);
    }

    @Test
    public void testCriarCartao_NOK() {
        CartaoRequestDTO requestDTO = new CartaoRequestDTO();
        Cartao cartao = new Cartao();
        when(modelMapper.map(requestDTO, Cartao.class)).thenReturn(cartao);
        doThrow(DataIntegrityViolationException.class).when(cartaoRepository).save(cartao);

        assertThrows(DataIntegrityViolationException.class, () -> cartaoService.criar(requestDTO));
    }

    @Test
    public void testGetSaldo_OK() {
        Cartao cartao = new Cartao();
        cartao.setSaldo(BigDecimal.valueOf(500));
        when(cartaoRepository.findByNumeroCartao("6549873025634501")).thenReturn(Optional.of(cartao));

        BigDecimal saldo = assertDoesNotThrow(() -> cartaoService.getSaldo("6549873025634501"));
        assertEquals(BigDecimal.valueOf(500), saldo);
    }

    @Test
    public void testGetSaldo_NOK() {
        when(cartaoRepository.findByNumeroCartao("6549873025634501")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartaoService.getSaldo("6549873025634501"));
    }

    @Test
    public void testGetCartaoByNumero() {
        Cartao cartao = new Cartao();
        when(cartaoRepository.findByNumeroCartao("6549873025634501")).thenReturn(Optional.of(cartao));

        Optional<Cartao> resultado = cartaoService.getCartaoByNumero("6549873025634501");
        assertTrue(resultado.isPresent());
    }

    @Test
    public void testSalvarCartao_OK() {
        Cartao cartao = new Cartao();

        assertDoesNotThrow(() -> cartaoService.salvar(cartao));
        verify(cartaoRepository, times(1)).save(cartao);
    }

    @Test
    public void testSalvarCartao_NOK() {
        Cartao cartao = new Cartao();
        doThrow(TransactionSystemException.class).when(cartaoRepository).save(cartao);

        assertThrows(TransactionSystemException.class, () -> cartaoService.salvar(cartao));
    }
}