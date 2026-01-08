package com.aflt.store.controller;


import com.aflt.store.entity.StockAFLT;
import com.aflt.store.service.ExcelParserService;
import com.aflt.store.service.StockAFLTService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/stocks")
public class StockAFLTController {
    private final StockAFLTService stockService;
    private final ExcelParserService excelParserService;

    public StockAFLTController(StockAFLTService stockService, ExcelParserService excelParserService) {
        this.stockService = stockService;
        this.excelParserService = excelParserService;
    }

    @GetMapping
    public String getAllStocks(@RequestParam(value = "all", required = false) Boolean showAll,
                               Model model) {
        if (showAll != null && showAll) {
            // Получить все записи из базы данных
            List<StockAFLT> stocks = stockService.getAllStocks();
            model.addAttribute("stocks", stocks);
        } else {
            // При обычной загрузке страницы не передаем список
            model.addAttribute("stocks", null);
        }
        return "stocks";
    }

    @GetMapping("/delete/{id}")
    public String deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
        return "redirect:/stocks";
    }

    @GetMapping("/search")
    public String searchByPartNumber(@RequestParam String partNumber, Model model) {
        model.addAttribute("stocks", stockService.getStocksByPartNumber(partNumber));
        model.addAttribute("newStock", new StockAFLT());
        return "stocks";
    }
    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("error", "Please select a file to upload");
            return "stocks";
        }

        try {
            List<StockAFLT> stocks = excelParserService.parseExcelFile(file);

            // Очищаем старые данные и сохраняем новые
            stockService.deleteAllStocks();
            stockService.saveAllStocks(stocks);

            model.addAttribute("message", "File uploaded successfully. " + stocks.size() + " records updated.");
            model.addAttribute("stocks", stocks); // Показываем загруженные данные

        } catch (Exception e) {
            model.addAttribute("error", "Error processing file: " + e.getMessage());
            e.printStackTrace();
        }

        return "stocks";
    }



}
