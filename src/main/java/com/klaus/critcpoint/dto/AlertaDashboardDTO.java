package com.klaus.critcpoint.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AlertaDashboardDTO {
    private String codigoAcao;
    private BigDecimal valorMinimo;
    private BigDecimal valorMaximo;
    private Double valorAtual;

    public AlertaDashboardDTO(String codigoAcao,  BigDecimal valorMinimo, BigDecimal valorMaximo, Double valorAtual) {
        this.codigoAcao = codigoAcao;
        this.valorMinimo = valorMinimo;
        this.valorMaximo = valorMaximo;
        this.valorAtual = valorAtual;
    }


}
