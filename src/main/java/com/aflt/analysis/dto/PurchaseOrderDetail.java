package com.aflt.analysis.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PurchaseOrderDetail {
    private String orderNo;
    private String extOrderNo;
    private LocalDate orderDate;
    private String partNumber;
    private Integer backorder;
    private LocalDate targetDate;
    private LocalDate confirmedDate;
    private String priority;
    private String responsible;
}
