package com.aflt.analysis.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class StockDetail {
    private String partNumber;
    private Double quantity;
    private LocalDate expireDate;
    private String owner;
    private String station;
    private String store;
    private String measureUnit;
}
