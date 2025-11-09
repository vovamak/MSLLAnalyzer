package com.aflt.store.controller;


import com.aflt.store.entity.StockAFLT;
import com.aflt.store.service.StockAFLTService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stocks")
public class StockAFLTController {
    private final StockAFLTService stockService;

    public StockAFLTController(StockAFLTService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public String getAllStocks(Model model) {
        model.addAttribute("stocks", stockService.getAllStocks());
        model.addAttribute("newStock", new StockAFLT());
        return "stocks";
    }

    @PostMapping("/add")
    public String addStock(@ModelAttribute StockAFLT stock) {
        stockService.saveStock(stock);
        return "redirect:/stocks";
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
}
