package com.klaus.critcpoint.service;

import com.klaus.critcpoint.dto.BrapiResponseDTO;
import com.klaus.critcpoint.dto.CotacaoDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AcaoService {
    public void buscarPreco(String codigoAcao){
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://brapi.dev/api/quote/" + codigoAcao;

        try {
            BrapiResponseDTO resposta = restTemplate.getForObject(url, BrapiResponseDTO.class);

            Double valorAtual = resposta.getResults().getFirst().getRegularMarketPrice();
            System.out.println("Valor atual da ação:" + valorAtual);

        } catch (Exception e) {
            System.out.println("ERRO: Não foi possível buscar o preço de " + codigoAcao);
            System.out.println(e.getMessage());
        }
    }
}
