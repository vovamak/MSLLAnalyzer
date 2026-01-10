package com.aflt.analysis.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class AnalysisResult {
    private String productCode;
    private String description;
    private List<String> lists;
    private Map<String, Double> stockByOwner; // owner -> total quantity
    private Map<String, Integer> purchaseOrdersByStatus; // status -> count
    private List<StockDetail> stockDetails;
    private List<PurchaseOrderDetail> purchaseOrderDetails;
    private Double totalStock;
    private Integer totalPurchaseOrders;
    private String measureUnit;



}
