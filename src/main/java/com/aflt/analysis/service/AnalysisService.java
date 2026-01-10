package com.aflt.analysis.service;

import com.aflt.analysis.dto.*;
import com.aflt.order.entity.PurchaseOrder;
import com.aflt.order.repository.PurchaseOrderRepository;
import com.aflt.product.entity.Product;
import com.aflt.product.entity.PartNumber;
import com.aflt.product.repository.ProductRepository;
import com.aflt.store.entity.StockAFLT;
import com.aflt.store.repository.StockAFLTRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalysisService {
    private final ProductRepository productRepository;

    private final StockAFLTRepository stockRepository;

    private final PurchaseOrderRepository purchaseOrderRepository;

    public AnalysisService(ProductRepository productRepository, StockAFLTRepository stockRepository, PurchaseOrderRepository purchaseOrderRepository) {
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    /**
     * Анализ по списку part numbers
     */
    public List<AnalysisResult> analyzeByPartNumbers(List<String> partNumbers) {
        List<AnalysisResult> results = new ArrayList<>();

        for (String partNumber : partNumbers) {
            // Найти продукт по part number
            Product product = productRepository.findByPartNumber(partNumber);

            if (product != null) {
                // Анализ для продукта
                results.add(analyzeProduct(product));
            } else {
                // Анализ только для одного part number
                results.add(analyzeSinglePart(partNumber));
            }
        }

        return results;
    }

    /**
     * Анализ по списку (MSLL, НЗ, etc)
     */
    public List<AnalysisResult> analyzeByList(String listName) {
        List<Product> products;

        if ("ALL".equals(listName)) {
            products = productRepository.findAll();
        } else {
            products = productRepository.findByListName(listName);
        }

        return products.stream()
                .map(this::analyzeProduct)
                .collect(Collectors.toList());
    }

    /**
     * Анализ продукта
     */
    private AnalysisResult analyzeProduct(Product product) {
        AnalysisResult result = new AnalysisResult();
        result.setProductCode(product.getProductCode());
        result.setDescription(product.getDescription());
        result.setLists(new ArrayList<>(product.getLists()));

        // Собираем все part numbers продукта
        List<String> partNumbers = product.getParts().stream()
                .map(PartNumber::getPartNumber)
                .collect(Collectors.toList());

        // Анализ остатков
        List<StockAFLT> allStocks = stockRepository.findByPartNumbers(partNumbers);
        analyzeStocks(result, allStocks);

        // Анализ заказов
        List<PurchaseOrder> allOrders = purchaseOrderRepository.findByPartNumbers(partNumbers);
        analyzePurchaseOrders(result, allOrders);

        // Основная единица измерения
        String mainMeasureUnit = product.getParts().stream()
                .findFirst()
                .map(PartNumber::getMeasureUnit)
                .orElse("pcs");
        result.setMeasureUnit(mainMeasureUnit);

        return result;
    }

    /**
     * Анализ для одиночного part number
     */
    private AnalysisResult analyzeSinglePart(String partNumber) {
        AnalysisResult result = new AnalysisResult();
        result.setProductCode("N/A");
        result.setDescription("No product found for part: " + partNumber);

        // Анализ остатков
        List<StockAFLT> stocks = stockRepository.findByPartNumber(partNumber);
        analyzeStocks(result, stocks);

        // Анализ заказов
        List<PurchaseOrder> orders = purchaseOrderRepository.findByPartNo(partNumber);
        analyzePurchaseOrders(result, orders);

        return result;
    }

    private void analyzeStocks(AnalysisResult result, List<StockAFLT> stocks) {
        // Детали по складу
        List<StockDetail> stockDetails = stocks.stream()
                .map(stock -> {
                    StockDetail detail = new StockDetail();
                    detail.setPartNumber(stock.getPartNumber());
                    detail.setQuantity(stock.getQuantity());
                    detail.setExpireDate(stock.getExpireDate());
                    detail.setOwner(stock.getOwner());
                    detail.setStation(stock.getStation());
                    detail.setStore(stock.getStore());
                    detail.setMeasureUnit(stock.getMeasureUnit());
                    return detail;
                })
                .collect(Collectors.toList());
        result.setStockDetails(stockDetails);

        // Суммарное количество по owner
        Map<String, Double> stockByOwner = stocks.stream()
                .collect(Collectors.groupingBy(
                        StockAFLT::getOwner,
                        Collectors.summingDouble(StockAFLT::getQuantity)
                ));
        result.setStockByOwner(stockByOwner);

        // Общее количество
        Double totalStock = stocks.stream()
                .mapToDouble(StockAFLT::getQuantity)
                .sum();
        result.setTotalStock(totalStock);
    }

    private void analyzePurchaseOrders(AnalysisResult result, List<PurchaseOrder> orders) {
        // Детали по заказам
        List<PurchaseOrderDetail> orderDetails = orders.stream()
                .map(order -> {
                    PurchaseOrderDetail detail = new PurchaseOrderDetail();
                    detail.setOrderNo(order.getOrderNo());
                    detail.setExtOrderNo(order.getExtOrderNo());
                    detail.setOrderDate(order.getDate());
                    detail.setPartNumber(order.getPartNo());
                    detail.setBackorder(order.getBackorder());
                    detail.setTargetDate(order.getTargetDate());
                    detail.setConfirmedDate(order.getConfirmedDate());
                    detail.setPriority(order.getPriority());
                    detail.setResponsible(order.getResponsible());
                    return detail;
                })
                .collect(Collectors.toList());
        result.setPurchaseOrderDetails(orderDetails);

        // Статистика по статусам (пример)
        Map<String, Integer> ordersByStatus = new HashMap<>();
        ordersByStatus.put("Backorder",
                (int) orders.stream().filter(o -> o.getBackorder() != null && o.getBackorder() > 0).count());
        ordersByStatus.put("Confirmed",
                (int) orders.stream().filter(o -> o.getConfirmedDate() != null).count());
        ordersByStatus.put("Total", orders.size());
        result.setPurchaseOrdersByStatus(ordersByStatus);

        result.setTotalPurchaseOrders(orders.size());
    }
}
