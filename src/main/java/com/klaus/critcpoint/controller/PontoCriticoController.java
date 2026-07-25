package com.klaus.critcpoint.controller;

import com.klaus.critcpoint.model.PontoCritico;
import com.klaus.critcpoint.repository.PontoCriticoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pontos-criticos")
public class PontoCriticoController {

    @Autowired
    private PontoCriticoRepository pontoCriticoRepository;

    @GetMapping
    public ResponseEntity<List<PontoCritico>> listarTodos() {
        List<PontoCritico> alertas = pontoCriticoRepository.findAll();
        return ResponseEntity.ok(alertas);
    }

    @PostMapping
    public ResponseEntity<?> salvarPontoCritico(@Valid @RequestBody PontoCritico pontoCritico) {

        if (pontoCritico.getValorMinimo().compareTo(pontoCritico.getValorMaximo()) >= 0) {
            return ResponseEntity.badRequest().body("Erro de Validação: O valor mínimo não pode ser maior ou igual ao valor máximo!");
        }

        PontoCritico salvo = pontoCriticoRepository.save(pontoCritico);
        return ResponseEntity.status(201).body(salvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAlerta(@PathVariable Long id) {

        if (!pontoCriticoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        pontoCriticoRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}