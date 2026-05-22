package com.klaus.critcpoint.controller;

import com.klaus.critcpoint.model.Usuario;
import com.klaus.critcpoint.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/usuarios")
    public Usuario salvarUsuario (@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}
