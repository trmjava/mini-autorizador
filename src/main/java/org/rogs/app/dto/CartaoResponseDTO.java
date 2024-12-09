package org.rogs.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class CartaoResponseDTO {

    @JsonProperty("senha")
    private String senha;

    @JsonProperty("numeroCartao")
    private String numeroCartao;

}
