package com.klaus.critcpoint.controller;

import com.klaus.critcpoint.dto.SimulacaoRequestDTO;
import com.klaus.critcpoint.dto.SimulacaoResponseDTO;
import com.klaus.critcpoint.service.SimulacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simulacao")
@CrossOrigin(origins = "http://localhost:4200")
public class SimulacaoController {

    @Autowired
    private SimulacaoService simulacaoService;

    @PostMapping
    public ResponseEntity<SimulacaoResponseDTO> simular(@Valid @RequestBody SimulacaoRequestDTO request) {
        SimulacaoResponseDTO resultado = simulacaoService.simular(request);
        return ResponseEntity.ok(resultado);
    }
}