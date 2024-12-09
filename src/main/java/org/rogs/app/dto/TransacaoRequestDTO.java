package org.rogs.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransacaoRequestDTO {
    private String numeroCartao;
    private String senhaCartao;
    private BigDecimal valor;

}