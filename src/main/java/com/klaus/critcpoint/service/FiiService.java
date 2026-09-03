package com.klaus.critcpoint.service;

import com.klaus.critcpoint.dto.FiiIndicadorDTO;
import com.klaus.critcpoint.dto.FiiResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FiiService {

    @Value("${brapi.token}")
    private String brapiToken;

    private FiiIndicadorDTO buscarIndicadores(String codigoFii) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://brapi.dev/api/v2/fii/indicators?symbols=" + codigoFii;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + brapiToken);
        HttpEntity<Void> entidade = new HttpEntity<>(headers);

        ResponseEntity<FiiResponseDTO> resposta = restTemplate.exchange(
                url, HttpMethod.GET, entidade, FiiResponseDTO.class
        );

        return resposta.getBody().getFiis().getFirst();
    }

    public Double buscarPreco(String codigoFii) {
        try {
            Double preco = buscarIndicadores(codigoFii).getPrice();
            return preco != null ? preco : 0.0;

        } catch (Exception e) {
            System.out.println("ERRO: Não foi possível buscar o preço do FII " + codigoFii);
            System.out.println(e.getMessage());
            return 0.0;
        }
    }

    public Double buscarDividendYield(String codigoFii) {
        try {
            Double dividendYield12m = buscarIndicadores(codigoFii).getDividendYield12m();
            return dividendYield12m != null ? dividendYield12m * 100 : 0.0;

        } catch (Exception e) {
            System.out.println("ERRO: Não foi possível buscar o dividend yield do FII " + codigoFii);
            System.out.println(e.getMessage());
            return 0.0;
        }
    }
}