package com.klaus.critcpoint.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TesouroResponseDTO {
    private List<TesouroDTO> results;
}