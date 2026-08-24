package com.klaus.critcpoint.service;

import com.klaus.critcpoint.dto.SimulacaoRequestDTO;
import com.klaus.critcpoint.dto.SimulacaoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimulacaoService {

    @Autowired
    private AcaoService acaoService;

    @Autowired
    private TesouroService tesouroService;

    public SimulacaoResponseDTO simular(SimulacaoRequestDTO request) {

        Double taxaAnual;

        if ("ACAO".equalsIgnoreCase(request.getTipoAtivo())) {
            taxaAnual = acaoService.buscarDividendYield(request.getCodigo());
        } else {
            taxaAnual = tesouroService.buscarTaxaAnual(request.getCodigo());
        }

        double taxaMensal = Math.pow(1 + (taxaAnual / 100), 1.0 / 12) - 1;

        double valorFinal;
        double valorInvestido;

        if ("UNICO".equalsIgnoreCase(request.getTipoAporte())) {
            valorInvestido = request.getValorAporte();
            valorFinal = request.getValorAporte()
                    * Math.pow(1 + taxaMensal, request.getQuantidadeMeses());

        } else {
            valorInvestido = request.getValorAporte() * request.getQuantidadeMeses();

            if (taxaMensal == 0) {
                valorFinal = valorInvestido;
            } else {
                valorFinal = request.getValorAporte()
                        * ((Math.pow(1 + taxaMensal, request.getQuantidadeMeses()) - 1) / taxaMensal);
            }
        }

        double totalRendimentos = valorFinal - valorInvestido;

        return new SimulacaoResponseDTO(taxaAnual, valorInvestido, valorFinal, totalRendimentos);
    }
}