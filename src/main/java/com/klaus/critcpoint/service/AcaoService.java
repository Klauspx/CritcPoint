package com.klaus.critcpoint.service;

import com.klaus.critcpoint.dto.BrapiResponseDTO;
import com.klaus.critcpoint.dto.CotacaoDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AcaoService {
    public Double buscarPreco(String codigoAcao){
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://brapi.dev/api/quote/" + codigoAcao;

        try {
            BrapiResponseDTO resposta = restTemplate.getForObject(url, BrapiResponseDTO.class);
            Double valorAtual = resposta.getResults().getFirst().getRegularMarketPrice();
            return valorAtual;

        } catch (Exception e) {
            System.out.println("ERRO: Não foi possível buscar o preço de " + codigoAcao);
            System.out.println(e.getMessage());
            return 0.0;
        }
    }

    public Double buscarDividendYield(String codigoAcao){
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://brapi.dev/api/quote/" + codigoAcao + "?modules=defaultKeyStatistics";

        try {
            BrapiResponseDTO resposta = restTemplate.getForObject(url, BrapiResponseDTO.class);
            CotacaoDTO cotacao = resposta.getResults().getFirst();

            if (cotacao.getDefaultKeyStatistics() == null
                    || cotacao.getDefaultKeyStatistics().getDividendYield() == null) {
                return 0.0;
            }

            return cotacao.getDefaultKeyStatistics().getDividendYield() * 100;

        } catch (Exception e) {
            System.out.println("ERRO: Não foi possível buscar o dividend yield de " + codigoAcao);
            System.out.println(e.getMessage());
            return 0.0;
        }
    }
}