package com.klaus.critcpoint.task;

import com.klaus.critcpoint.model.PontoCritico;
import com.klaus.critcpoint.repository.PontoCriticoRepository;
import com.klaus.critcpoint.service.AcaoService;
import com.klaus.critcpoint.service.EmailService;
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
    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 10-17/2 * * MON-FRI")
    public void checarLimites(){

        List<PontoCritico> alertas = pontoCriticoRepository.findAll();

        for (PontoCritico alertaAtual : alertas) {

            Double precoAtual = acaoService.buscarPreco(alertaAtual.getCodigoAcao());

            BigDecimal precoConvertido = BigDecimal.valueOf(precoAtual);

            String emailDono = alertaAtual.getUsuario().getEmail();

            if (precoConvertido.compareTo(alertaAtual.getValorMaximo()) >= 0){
                String mensagemMax = "Olá! A ação " + alertaAtual.getCodigoAcao() + " disparou e atingiu o seu limite MÁXIMO estipulado! O preço atual na Brapi é de R$ " + precoAtual;

                emailService.sendMail(emailDono, "CritcPoint: Alerta de ALTA!!!", mensagemMax);

                pontoCriticoRepository.delete(alertaAtual);
                System.out.println("Alerta de " + alertaAtual.getCodigoAcao() + " disparado e apagado com sucesso.");

            } else if (precoConvertido.compareTo(alertaAtual.getValorMinimo()) <= 0) {
                String mensagemMin = "Olá! A ação " + alertaAtual.getCodigoAcao() + "caiu e atingiu o seu valor MÍNIMO estipulado! O preço atual na Brapi é de R$ " + precoAtual;

                emailService.sendMail(emailDono, "CritcPoint: Alerta de BAIXA!!!", mensagemMin);

                pontoCriticoRepository.delete(alertaAtual);
                System.out.println("Alerta de " + alertaAtual.getCodigoAcao() + " disparado e apagado com sucesso.");
            }
        }
    }

}
