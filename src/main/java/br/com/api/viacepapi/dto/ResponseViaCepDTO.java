package br.com.api.viacepapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseViaCepDTO {
    private String cep;

    @JsonProperty("rua")
    private String logradouro;

    private String complemento;

    private String bairro;

    @JsonProperty("cidade")
    private String localidade;

    @JsonProperty("estado")
    private String uf;

    private String frete;

}
