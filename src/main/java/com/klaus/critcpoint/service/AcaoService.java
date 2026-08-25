package com.klaus.critcpoint.service;

import com.klaus.critcpoint.dto.BrapiResponseDTO;
import com.klaus.critcpoint.dto.CotacaoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AcaoService {

    @Value("${brapi.token}")
    private String brapiToken;

    private HttpEntity<Void> montarRequisicaoAutenticada() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + brapiToken);
        return new HttpEntity<>(headers);
    }

    public Double buscarPreco(String codigoAcao){
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://brapi.dev/api/quote/" + codigoAcao;

        try {
            ResponseEntity<BrapiResponseDTO> resposta = restTemplate.exchange(
                    url, HttpMethod.GET, montarRequisicaoAutenticada(), BrapiResponseDTO.class
            );

            Double valorAtual = resposta.getBody().getResults().getFirst().getRegularMarketPrice();
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
            ResponseEntity<BrapiResponseDTO> resposta = restTemplate.exchange(
                    url, HttpMethod.GET, montarRequisicaoAutenticada(), BrapiResponseDTO.class
            );

            CotacaoDTO cotacao = resposta.getBody().getResults().getFirst();

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