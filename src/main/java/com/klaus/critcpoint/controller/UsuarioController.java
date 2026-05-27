package com.klaus.critcpoint.controller;

import com.klaus.critcpoint.dto.AlertaDashboardDTO;
import com.klaus.critcpoint.model.PontoCritico;
import com.klaus.critcpoint.model.Usuario;
import com.klaus.critcpoint.repository.PontoCriticoRepository;
import com.klaus.critcpoint.repository.UsuarioRepository;
import com.klaus.critcpoint.service.AcaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PontoCriticoRepository pontoCriticoRepository;

    @Autowired
    private AcaoService acaoService;

    @PostMapping("/usuarios")
    public Usuario salvarUsuario (@Valid @RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @GetMapping("/usuarios")
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @DeleteMapping("/usuarios/{id}")
    public void deleteUsuario(@Valid @PathVariable Long id) {
        usuarioRepository.deleteById(id);
    }

    @PutMapping("/usuarios/{id}")
    public Usuario atualizarUsuario(@PathVariable Long id,@Valid @RequestBody Usuario usuarioNovo) {
        Usuario usuarioExistente = usuarioRepository.findById(id).get();
        usuarioExistente.setNome(usuarioNovo.getNome());
        usuarioExistente.setEmail(usuarioNovo.getEmail());
        usuarioExistente.setSenha(usuarioNovo.getSenha());
        return usuarioRepository.save(usuarioExistente);
    }

    @GetMapping("usuarios/{id}/dashboard")
    public ResponseEntity<List<AlertaDashboardDTO>> verDashboard(@PathVariable Long id){

        List<PontoCritico> alertasDoUsuario = pontoCriticoRepository.findByUsuarioId(id);

        List<AlertaDashboardDTO> listaDeEntrega = new ArrayList<>();

        for (PontoCritico alerta : alertasDoUsuario) {

            Double precoAtual = acaoService.buscarPreco(alerta.getCodigoAcao());


            AlertaDashboardDTO dto = new AlertaDashboardDTO(
                    alerta.getCodigoAcao(),
                    alerta.getValorMinimo(),
                    alerta.getValorMaximo(),
                    precoAtual
            );

            listaDeEntrega.add(dto);
        }
        return ResponseEntity.ok(listaDeEntrega);
    }
}
