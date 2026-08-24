package com.klaus.critcpoint.service;

import com.klaus.critcpoint.dto.MacroResponseDTO;
import com.klaus.critcpoint.dto.TesouroDTO;
import com.klaus.critcpoint.dto.TesouroResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TesouroService {

    public Double buscarTaxaAnual(String slugTitulo) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://brapi.dev/api/v2/treasury/indicators?symbols=" + slugTitulo;

        try {
            TesouroResponseDTO resposta = restTemplate.getForObject(url, TesouroResponseDTO.class);
            TesouroDTO titulo = resposta.getResults().getFirst();

            Double taxaBruta = titulo.getBuyRate();
            String tipoTaxa = titulo.getRateInfo() != null ? titulo.getRateInfo().getRateType() : null;

            if ("spreadOverSelic".equals(tipoTaxa)) {
                try {
                    Double selicAtual = buscarSelicAtual(restTemplate);
                    return ((1 + selicAtual / 100) * (1 + taxaBruta / 100) - 1) * 100;
                } catch (Exception erroSelic) {
                    System.out.println("AVISO: não foi possível buscar a Selic atual (precisa de token). " +
                            "Usando só o spread como aproximação.");
                    return taxaBruta;
                }
            }

            return taxaBruta;

        } catch (Exception e) {
            System.out.println("ERRO: Não foi possível buscar a taxa de " + slugTitulo);
            System.out.println(e.getMessage());
            return 0.0;
        }
    }

    private Double buscarSelicAtual(RestTemplate restTemplate) {
        String url = "https://brapi.dev/api/v2/macro/latest?symbols=selic";
        MacroResponseDTO resposta = restTemplate.getForObject(url, MacroResponseDTO.class);
        return resposta.getResults().getFirst().getLatest().getValue();
    }
}