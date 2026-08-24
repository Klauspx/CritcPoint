package com.klaus.critcpoint.controller;

import com.klaus.critcpoint.dto.LoginDTO;
import com.klaus.critcpoint.model.Usuario;
import com.klaus.critcpoint.repository.PontoCriticoRepository;
import com.klaus.critcpoint.repository.UsuarioRepository;
import com.klaus.critcpoint.service.AcaoService;
import org.springframework.http.MediaType;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @MockitoBean
    private PontoCriticoRepository pontoCriticoRepository;

    @MockitoBean
    private AcaoService acaoService;

    @Test
    void deveListarUsuarioCadastrado() throws Exception {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Klaus");
        usuario1.setEmail("klaus@test.com");
        usuario1.setSenha("123456");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNome("Caua");
        usuario2.setEmail("caua@test.com");
        usuario2.setSenha("654321");

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario1, usuario2));

        mockMvc.perform(get("/usuarios"))

        .andExpect(status().isOk())

        .andExpect(jsonPath("$.length()").value(2))

        .andExpect(jsonPath("$[0].nome").value("Klaus"))

        .andExpect(jsonPath("$[1].email").value("caua@test.com"));
    }

    @Test
    void deveFazerLoginComSucesso() throws Exception {
        Usuario usuarioFalso = new Usuario();
        usuarioFalso.setId(1L);
        usuarioFalso.setNome("Klaus");
        usuarioFalso.setEmail("klaus@test.com");
        usuarioFalso.setSenha("123456");

        when(usuarioRepository.findByEmail("klaus@test.com")).thenReturn(Optional.of(usuarioFalso));

        LoginDTO loginEnviado = new LoginDTO();
        loginEnviado.setEmail("klaus@test.com");
        loginEnviado.setSenha("123456");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginEnviado)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.senha").doesNotExist());

    }
}
