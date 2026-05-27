package com.klaus.critcpoint.repository;

import com.klaus.critcpoint.model.PontoCritico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PontoCriticoRepository extends JpaRepository<PontoCritico, Long> {

    List<PontoCritico> findByUsuarioId(Long id);
}
