package com.klaus.critcpoint.service;

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

    public Double buscarDividendYield(String codigoFii) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://brapi.dev/api/v2/fii/indicators?symbols=" + codigoFii;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + brapiToken);
            HttpEntity<Void> entidade = new HttpEntity<>(headers);

            ResponseEntity<FiiResponseDTO> resposta = restTemplate.exchange(
                    url, HttpMethod.GET, entidade, FiiResponseDTO.class
            );

            Double dividendYield12m = resposta.getBody().getFiis().getFirst().getDividendYield12m();

            if (dividendYield12m == null) {
                return 0.0;
            }

            return dividendYield12m * 100;

        } catch (Exception e) {
            System.out.println("ERRO: Não foi possível buscar o dividend yield do FII " + codigoFii);
            System.out.println(e.getMessage());
            return 0.0;
        }
    }
}