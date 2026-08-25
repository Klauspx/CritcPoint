package com.klaus.critcpoint.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FiiResponseDTO {
    private List<FiiIndicadorDTO> fiis; // repara: essa chave é "fiis", diferente de "results"!
}