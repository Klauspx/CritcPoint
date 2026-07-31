package com.klaus.critcpoint.task;

import com.klaus.critcpoint.model.PontoCritico;
import com.klaus.critcpoint.repository.PontoCriticoRepository;
import com.klaus.critcpoint.service.AcaoService;
import com.klaus.critcpoint.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

            if (precoAtual == null || precoAtual <= 0.0){
                System.out.println("A ação " + alertaAtual.getCodigoAcao() + " está em manutenção nesse momento");
                continue;
            }

            BigDecimal precoConvertido = BigDecimal.valueOf(precoAtual);

            String emailDono = alertaAtual.getUsuario().getEmail();

            boolean precisaSalvar = false;

            if (precoConvertido.compareTo(alertaAtual.getValorMaximo()) >= 0 && !alertaAtual.isAlertaAltaEnviado()){
                String mensagemMax = "Olá! A ação " + alertaAtual.getCodigoAcao() + " disparou e atingiu o seu limite MÁXIMO estipulado! O preço atual na Brapi é de R$ " + precoAtual;

                emailService.sendMail(emailDono, "CritcPoint: Alerta de ALTA!!!", mensagemMax);

                alertaAtual.setAlertaAltaEnviado(true);
                precisaSalvar = true;
                System.out.println("Alerta de ALTA para " + alertaAtual.getCodigoAcao() + " disparado com sucesso.");

            }
            else if (precoConvertido.compareTo(alertaAtual.getValorMinimo()) <= 0 && !alertaAtual.isAlertaBaixaEnviado()) {
                String mensagemMin = "Olá! A ação " + alertaAtual.getCodigoAcao() + " caiu e atingiu o seu valor MÍNIMO estipulado! O preço atual na Brapi é de R$ " + precoAtual;

                emailService.sendMail(emailDono, "CritcPoint: Alerta de BAIXA!!!", mensagemMin);

                alertaAtual.setAlertaBaixaEnviado(true);
                precisaSalvar = true;
                System.out.println("Alerta de BAIXA para " + alertaAtual.getCodigoAcao() + " disparado com sucesso.");
            }
            else if (precoConvertido.compareTo(alertaAtual.getValorMaximo()) < 0 && precoConvertido.compareTo(alertaAtual.getValorMinimo()) > 0) {
                if (alertaAtual.isAlertaBaixaEnviado() || alertaAtual.isAlertaAltaEnviado()) {
                    alertaAtual.setAlertaBaixaEnviado(false);
                    alertaAtual.setAlertaAltaEnviado(false);
                    precisaSalvar = true;
                    System.out.println("Armadilha rearmada para a ação " + alertaAtual.getCodigoAcao() + ".");
                }
            }

            if (precisaSalvar) {
                pontoCriticoRepository.save(alertaAtual);
            }
        }
    }
}
