package org.rogs.app.integrado;

import org.junit.jupiter.api.Test;
import org.rogs.app.model.Cartao;
import org.rogs.app.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class ConcorrenciaTeste {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Test
    public void testOptimisticLocking() {
        Cartao cartao = new Cartao();
        cartao.setNumeroCartao("6549873025634501");
        cartao.setSenha("1234");
        cartao.setSaldo(new BigDecimal("25.00"));
        cartaoRepository.save(cartao);

        Cartao cartao1 = cartaoRepository.findById(cartao.getId()).orElseThrow();
        Cartao cartao2 = cartaoRepository.findById(cartao.getId()).orElseThrow();

        cartao1.setSaldo(cartao1.getSaldo().subtract(new BigDecimal("25.00")));
        cartaoRepository.save(cartao1); // Deve funcionar

        cartao2.setSaldo(cartao2.getSaldo().subtract(new BigDecimal("25.00")));

        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            cartaoRepository.save(cartao2);
        });
    }
}