package com.klaus.critcpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SimulacaoResponseDTO {
    private Double taxaAnualUtilizada;
    private Double valorInvestido;
    private Double valorFinal;
    private Double totalRendimentos;
}