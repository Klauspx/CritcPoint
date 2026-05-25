package com.klaus.critcpoint.task;

import com.klaus.critcpoint.model.PontoCritico;
import com.klaus.critcpoint.repository.PontoCriticoRepository;
import com.klaus.critcpoint.service.AcaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AlertaTask {
    @Autowired
    private PontoCriticoRepository pontoCriticoRepository;
    @Autowired
    private AcaoService acaoService;

    @Scheduled(fixedRate = 60000)
    public void checarLimites(){

        List<PontoCritico> alertas = pontoCriticoRepository.findAll();

        for (PontoCritico alertaAtual : alertas) {

            Double precoAtual = acaoService.buscarPreco(alertaAtual.getCodigoAcao());

            BigDecimal precoConvertido = BigDecimal.valueOf(precoAtual);

            if (precoConvertido.compareTo(alertaAtual.getValorMaximo()) >= 0){
                System.out.println(" ALERTA: Limite máximo atingido! Preparando para enviar mensagem ao usuário...");
            } else if (precoConvertido.compareTo(alertaAtual.getValorMinimo()) <= 0) {
                System.out.println(" ALERTA: Limite mínimo atingido! Preparando para enviar mensagem ao usuário...");
            }
        }
    }

}
