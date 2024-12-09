package org.rogs.app.integrado;

import org.junit.jupiter.api.Test;
import org.rogs.app.dto.TransacaoRequestDTO;
import org.rogs.app.model.Cartao;
import org.rogs.app.repository.CartaoRepository;
import org.rogs.app.service.CartaoService;
import org.rogs.app.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class OperacaoTest {

    @Autowired
    private CartaoService service;

    @Autowired
    private TransacaoService transacaoService;

    @Test
    public void testFuncionalidade() throws Exception {
        Cartao cartao = new Cartao();
        cartao.setNumeroCartao("6549873025634501");
        cartao.setSenha("1234");
        cartao.setSaldo(new BigDecimal("500.00"));
        service.salvar(cartao);

        TransacaoRequestDTO requestDTO = new TransacaoRequestDTO();
        requestDTO.setSenhaCartao("1234");
        requestDTO.setValor(java.math.BigDecimal.valueOf(10));
        requestDTO.setNumeroCartao(cartao.getNumeroCartao());

        transacaoService.processarTransacao(requestDTO);

        assert(service.getSaldo(cartao.getNumeroCartao()).equals(new BigDecimal("490.00")));

        requestDTO.setValor(java.math.BigDecimal.valueOf(20));
        transacaoService.processarTransacao(requestDTO);
        assert(service.getSaldo(cartao.getNumeroCartao()).equals(new BigDecimal("470.00")));

        requestDTO.setValor(java.math.BigDecimal.valueOf(1000));

        assertEquals("SALDO_INSUFICIENTE",
            assertThrows(Exception.class, () -> {transacaoService.processarTransacao(requestDTO);}).getMessage());

    }


}
