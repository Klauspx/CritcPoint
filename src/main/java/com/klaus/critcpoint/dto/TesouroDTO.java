package com.klaus.critcpoint.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TesouroDTO {
    private String symbol;
    private String bondType;
    private Double buyRate;
    private RateInfoDTO rateInfo;
}