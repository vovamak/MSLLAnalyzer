package com.aflt.store.service;

import com.aflt.store.entity.StockAFLT;
import com.aflt.store.repository.StockAFLTRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockAFLTService {
    private final StockAFLTRepository stockRepository;

    public StockAFLTService(StockAFLTRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<StockAFLT> getAllStocks() {
        return stockRepository.findAll();
    }

    public Optional<StockAFLT> getStockById(Long id) {
        return stockRepository.findById(id);
    }

    public StockAFLT saveStock(StockAFLT stock) {
        return stockRepository.save(stock);
    }

    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }

    public List<StockAFLT> getStocksByPartNumber(String partNumber) {
        return stockRepository.findByPartNumber(partNumber);
    }

    public List<StockAFLT> getStocksByStation(String station) {
        return stockRepository.findByStation(station);
    }
}

