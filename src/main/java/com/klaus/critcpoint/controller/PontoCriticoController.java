package com.klaus.critcpoint.controller;

import com.klaus.critcpoint.model.PontoCritico;
import com.klaus.critcpoint.repository.PontoCriticoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
