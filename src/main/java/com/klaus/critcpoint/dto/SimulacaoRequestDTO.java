package com.klaus.critcpoint.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimulacaoRequestDTO {

    @NotBlank
    private String tipoAtivo;

    @NotBlank
    private String codigo;

    @NotNull
    @Positive
    private Double valorAporte;

    @NotBlank
    private String tipoAporte;

    @NotNull
    @Positive
    private Integer quantidadeMeses;
}