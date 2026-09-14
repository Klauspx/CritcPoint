package com.klaus.critcpoint.controller;

import com.klaus.critcpoint.model.PontoCritico;
import com.klaus.critcpoint.repository.PontoCriticoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PontoCriticoController.class)
class PontoCriticoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PontoCriticoRepository pontoCriticoRepository;

    @Test
    void deveCriarAlertaComSucesso() throws Exception {
        PontoCritico novoAlerta = new PontoCritico();
        novoAlerta.setCodigoAcao("PETR4");
        novoAlerta.setValorMinimo(new BigDecimal("30.00"));
        novoAlerta.setValorMaximo(new BigDecimal("40.00"));

        PontoCritico alertaSalvo = new PontoCritico();
        alertaSalvo.setId(1L);
        alertaSalvo.setCodigoAcao("PETR4");
        alertaSalvo.setValorMinimo(new BigDecimal("30.00"));
        alertaSalvo.setValorMaximo(new BigDecimal("40.00"));

        when(pontoCriticoRepository.save(any(PontoCritico.class))).thenReturn(alertaSalvo);

        mockMvc.perform(post("/pontos-criticos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoAlerta)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.codigoAcao").value("PETR4"));
    }

    @Test
    void deveRecusarAlertaComValorMinimoMaiorQueMaximo() throws Exception {
        PontoCritico alertaInvalido = new PontoCritico();
        alertaInvalido.setCodigoAcao("PETR4");
        alertaInvalido.setValorMinimo(new BigDecimal("50.00")); // mínimo MAIOR que máximo
        alertaInvalido.setValorMaximo(new BigDecimal("40.00"));

        mockMvc.perform(post("/pontos-criticos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alertaInvalido)))
                .andExpect(status().isBadRequest());

        // confirma que, como a validação falhou, nunca chegou a tentar salvar
        verify(pontoCriticoRepository, never()).save(any());
    }

    @Test
    void deveExcluirAlertaExistente() throws Exception {
        when(pontoCriticoRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/pontos-criticos/1"))
                .andExpect(status().isNoContent());

        verify(pontoCriticoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveDevolver404AoExcluirAlertaInexistente() throws Exception {
        when(pontoCriticoRepository.existsById(999L)).thenReturn(false);

        mockMvc.perform(delete("/pontos-criticos/999"))
                .andExpect(status().isNotFound());

        // confirma que nunca tentou deletar algo que não existe
        verify(pontoCriticoRepository, never()).deleteById(any());
    }
}