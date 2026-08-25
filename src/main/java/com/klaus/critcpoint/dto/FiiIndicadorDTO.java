package com.klaus.critcpoint.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FiiIndicadorDTO {
    private String symbol;
    private Double price;
    private Double dividendYield12m; // fração decimal, ex: 0.0843 = 8.43%
}