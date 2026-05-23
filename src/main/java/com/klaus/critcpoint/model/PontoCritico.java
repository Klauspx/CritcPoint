package com.klaus.critcpoint.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class PontoCritico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String codigoAcao;

    @NotNull
    private BigDecimal valorMinimo;

    @NotNull
    private BigDecimal valorMaximo;


    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
