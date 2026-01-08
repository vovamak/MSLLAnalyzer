package com.aflt.store.service;

import com.aflt.store.entity.StockAFLT;
import com.aflt.store.repository.StockAFLTRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class StockAFLTService {
    private final StockAFLTRepository stockRepository;

    @PersistenceContext
    private EntityManager entityManager;



    public StockAFLTService(StockAFLTRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<StockAFLT> getAllStocks() {
        return stockRepository.findAll();
    }


    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }

    public List<StockAFLT> getStocksByPartNumber(String partNumber) {
        return stockRepository.findByPartNumber(partNumber);
    }
    @Transactional
    public void deleteAllStocks() {
        stockRepository.deleteAll();
        // Сбрасываем последовательность к 1
        entityManager.createNativeQuery("ALTER SEQUENCE stock_aflt_id_seq RESTART WITH 1").executeUpdate();
    }
    public void saveAllStocks(List<StockAFLT> stocks) {
        stockRepository.saveAll(stocks);
    }


}

