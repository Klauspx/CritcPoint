package com.klaus.critcpoint.controller;

import com.klaus.critcpoint.model.PontoCritico;
import com.klaus.critcpoint.repository.PontoCriticoRepository;
import com.klaus.critcpoint.service.AcaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/pontos-criticos")
public class PontoCriticoController {
    @Autowired
    private PontoCriticoRepository pontoCriticoRepository;

    @Autowired
    private AcaoService acaoService;

    @PostMapping
    public PontoCritico salvarPontoCritico(@Valid @RequestBody PontoCritico pontoCritico){

       Double precoAtual = acaoService.buscarPreco(pontoCritico.getCodigoAcao());

        BigDecimal precoConvertido = BigDecimal.valueOf(precoAtual);

        if (precoConvertido.compareTo(pontoCritico.getValorMaximo()) >= 0){
            System.out.println("🚨 ALERTA: Limite atingido! Preparando para enviar mensagem ao usuário...");
        } else if (precoConvertido.compareTo(pontoCritico.getValorMinimo()) <= 0) {
            System.out.println("🚨 ALERTA: Limite atingido! Preparando para enviar mensagem ao usuário...");
        }

        return pontoCriticoRepository.save(pontoCritico);
    }

}
