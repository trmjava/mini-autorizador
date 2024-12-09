package org.rogs.app.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.rogs.app.dto.CartaoRequestDTO;
import org.rogs.app.model.Cartao;
import org.rogs.app.repository.CartaoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionSystemException;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartaoService {

    private final CartaoRepository repo;
    private final ModelMapper modelMapper;

    public CartaoService(CartaoRepository repo, ModelMapper modelMapper) {
        this.repo = repo;
        this.modelMapper = modelMapper;
    }
    @Transactional
    public void criar(CartaoRequestDTO request) throws DataIntegrityViolationException {
        Cartao cartao = modelMapper.map(request, Cartao.class);
        cartao.setSaldo(BigDecimal.valueOf(500.0));
        repo.save(cartao);
    }

    public BigDecimal getSaldo(String numeroCartao) throws EntityNotFoundException{
        Optional<Cartao> cartao = repo.findByNumeroCartao(numeroCartao);
        return cartao.map(Cartao::getSaldo).orElseThrow(EntityNotFoundException::new);
    }

    public Optional<Cartao> getCartaoByNumero(String numeroCartao) {
        return repo.findByNumeroCartao(numeroCartao);
    }

    public void salvar(Cartao cartao) throws TransactionException {
        try {
            repo.save(cartao);
        } catch (TransactionException e) {
            throw new TransactionSystemException("SALDO_INSUFICIENTE");
        }
    }
}
