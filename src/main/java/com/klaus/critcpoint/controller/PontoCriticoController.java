package com.klaus.critcpoint.controller;

import com.klaus.critcpoint.dto.AlertaDashboardDTO;
import com.klaus.critcpoint.model.PontoCritico;
import com.klaus.critcpoint.repository.PontoCriticoRepository;
import com.klaus.critcpoint.service.AcaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pontos-criticos")
public class PontoCriticoController {
    @Autowired
    private PontoCriticoRepository pontoCriticoRepository;

    @PostMapping
    public PontoCritico salvarPontoCritico(@Valid @RequestBody PontoCritico pontoCritico){
        return pontoCriticoRepository.save(pontoCritico);
    }
}
