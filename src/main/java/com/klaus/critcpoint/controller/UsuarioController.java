package com.klaus.critcpoint.controller;

import com.klaus.critcpoint.model.Usuario;
import com.klaus.critcpoint.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/usuarios")
    public Usuario salvarUsuario (@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @GetMapping("/usuarios")
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @DeleteMapping("/usuarios/{id}")
    public void deleteUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
    }

    @PutMapping("/usuarios/{id}")
    public Usuario atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioNovo) {
        Usuario usuarioExistente = usuarioRepository.findById(id).get();
        usuarioExistente.setNome(usuarioNovo.getNome());
        usuarioExistente.setEmail(usuarioNovo.getEmail());
        usuarioExistente.setSenha(usuarioNovo.getSenha());
        return usuarioRepository.save(usuarioExistente);
    }
}
