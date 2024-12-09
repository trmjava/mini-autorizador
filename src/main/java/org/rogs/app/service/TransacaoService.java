package org.rogs.app.service;

import jakarta.persistence.EntityNotFoundException;
import org.rogs.app.dto.TransacaoRequestDTO;
import org.rogs.app.model.Cartao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;

import java.util.stream.Stream;

@Service
public class TransacaoService {

    private final CartaoService service;

    public TransacaoService(CartaoService service) {
        this.service = service;
    }

    public void processarTransacao(TransacaoRequestDTO requestDTO) throws Exception {

        try {

            var cartao = service.getCartaoByNumero(requestDTO.getNumeroCartao());
            var senha = cartao.map(Cartao::getSenha).orElseThrow(EntityNotFoundException::new);
            Stream.of(senha).filter(s -> s.equals(requestDTO.getSenhaCartao())).findFirst().orElseThrow(IllegalArgumentException::new);

            var saldo = cartao.map(Cartao::getSaldo).orElseThrow(EntityNotFoundException::new);

            saldo = saldo.subtract(requestDTO.getValor());
            cartao.get().setSaldo(saldo);
            service.salvar(cartao.get());
        }
        catch (EntityNotFoundException e){
            throw new Exception("CARTAO_INEXISTENTE");
        }
        catch (IllegalArgumentException e){
            throw new Exception("SENHA_INVALIDA");
        }
        catch (TransactionException e){
            throw new Exception(e.getMessage());
        }
    }
}
