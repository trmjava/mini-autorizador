package org.rogs.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartaoRequestDTO {

    @JsonProperty("numeroCartao")
    private String numeroCartao;

    @JsonProperty("senha")
    private String senha;
}
