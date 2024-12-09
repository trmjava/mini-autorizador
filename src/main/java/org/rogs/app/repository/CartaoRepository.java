package org.rogs.app.repository;

import org.rogs.app.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {
    Optional<Cartao> findByNumeroCartao(String numeroCartao);

}
