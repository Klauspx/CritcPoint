package com.klaus.critcpoint.repository;

import com.klaus.critcpoint.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
