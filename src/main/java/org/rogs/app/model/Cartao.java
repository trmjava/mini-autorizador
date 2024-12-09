package org.rogs.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "cartao")
public class Cartao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 16)
    private String numeroCartao;

    @Column(nullable = false, length = 4)
    private String senha;

    @Column(nullable = false)
    @Check(constraints = "saldo >= 0")
    @Min(0)
    private BigDecimal saldo;

    @Version
    private Long versao;

}
