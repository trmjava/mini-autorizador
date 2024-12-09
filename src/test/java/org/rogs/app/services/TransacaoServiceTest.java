package org.rogs.app.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.rogs.app.model.Cartao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.rogs.app.service.CartaoService;
import org.rogs.app.service.TransacaoService;
import org.rogs.app.dto.TransacaoRequestDTO;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.TransactionSystemException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransacaoServiceTest {

    @MockBean
    private CartaoService cartaoService;

    @Autowired
    private TransacaoService transacaoService;

    @Test
    public void testProcessarTransacao_OK() throws Exception {
        TransacaoRequestDTO requestDTO = new TransacaoRequestDTO();
        Cartao cartao = new Cartao();

        requestDTO.setSenhaCartao("1234");
        requestDTO.setValor(java.math.BigDecimal.valueOf(10));
        cartao.setSenha("1234");
        cartao.setSaldo(java.math.BigDecimal.valueOf(500));

        when(cartaoService.getCartaoByNumero(any())).thenReturn(java.util.Optional.of(cartao));
        doNothing().when(cartaoService).salvar(any());

        transacaoService.processarTransacao(requestDTO);
    }

    @Test
    public void testProcessarTransacao_CARTAO_INEXISTENTE() throws Exception {
        TransacaoRequestDTO requestDTO = new TransacaoRequestDTO();

        requestDTO.setSenhaCartao("1234");
        requestDTO.setValor(java.math.BigDecimal.valueOf(10));

        when(cartaoService.getCartaoByNumero(any())).thenThrow(new EntityNotFoundException(""));

        assertEquals("CARTAO_INEXISTENTE",
           assertThrows(Exception.class, () -> transacaoService.processarTransacao(requestDTO)).getMessage());
    }
    @Test
    public void testProcessarTransacao_SENHA_INVALIDA() throws Exception {
        TransacaoRequestDTO requestDTO = new TransacaoRequestDTO();

        requestDTO.setSenhaCartao("1234");
        requestDTO.setValor(java.math.BigDecimal.valueOf(10));

        when(cartaoService.getCartaoByNumero(any())).thenThrow(new IllegalArgumentException(""));

        assertEquals("SENHA_INVALIDA",
                assertThrows(Exception.class, () -> transacaoService.processarTransacao(requestDTO)).getMessage());
    }
    @Test
    public void testProcessarTransacao_SALDO_INSUFICIENTE() throws Exception {
        TransacaoRequestDTO requestDTO = new TransacaoRequestDTO();
        Cartao cartao = new Cartao();

        requestDTO.setSenhaCartao("1234");
        requestDTO.setValor(java.math.BigDecimal.valueOf(10));
        cartao.setSenha("1234");
        cartao.setSaldo(java.math.BigDecimal.valueOf(500));

        when(cartaoService.getCartaoByNumero(any())).thenReturn(java.util.Optional.of(cartao));
        doThrow(new TransactionSystemException("SALDO_INSUFICIENTE")).when(cartaoService).salvar(any());

        assertEquals("SALDO_INSUFICIENTE",
                assertThrows(Exception.class, () -> transacaoService.processarTransacao(requestDTO)).getMessage());
    }

}