package com.aflt.order.controller;

import com.aflt.order.entity.PurchaseOrder;
import com.aflt.order.service.PurchaseOrderExcelParserService;
import com.aflt.order.service.PurchaseOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/purchase-orders")
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;
    private final PurchaseOrderExcelParserService excelParserService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService,
                                   PurchaseOrderExcelParserService excelParserService) {
        this.purchaseOrderService = purchaseOrderService;
        this.excelParserService = excelParserService;
    }

    @GetMapping
    public String getAllPurchaseOrders(@RequestParam(value = "all", required = false) Boolean showAll,
                                       Model model) {
        if (showAll != null && showAll) {
            List<PurchaseOrder> purchaseOrders = purchaseOrderService.getAllPurchaseOrders();
            model.addAttribute("purchaseOrders", purchaseOrders);
        } else {
            model.addAttribute("purchaseOrders", null);
        }
        return "purchase-orders";
    }

    @GetMapping("/delete/{id}")
    public String deletePurchaseOrder(@PathVariable Long id) {
        purchaseOrderService.deletePurchaseOrder(id);
        return "redirect:/purchase-orders";
    }

    @GetMapping("/search")
    public String searchByPartNo(@RequestParam String partNo, Model model) {
        model.addAttribute("purchaseOrders", purchaseOrderService.getPurchaseOrdersByPartNo(partNo));
        return "purchase-orders";
    }

    @GetMapping("/search-by-order")
    public String searchByOrderNo(@RequestParam String orderNo, Model model) {
        model.addAttribute("purchaseOrders", purchaseOrderService.getPurchaseOrdersByOrderNo(orderNo));
        return "purchase-orders";
    }

    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("error", "Please select a file to upload");
            return "purchase-orders";
        }

        try {
            List<PurchaseOrder> purchaseOrders = excelParserService.parseExcelFile(file);

            // Очищаем старые данные и сохраняем новые
            purchaseOrderService.deleteAllPurchaseOrders();
            purchaseOrderService.saveAllPurchaseOrders(purchaseOrders);

            model.addAttribute("message", "File uploaded successfully. " + purchaseOrders.size() + " records updated.");
            model.addAttribute("purchaseOrders", purchaseOrders);

        } catch (Exception e) {
            model.addAttribute("error", "Error processing file: " + e.getMessage());
            e.printStackTrace();
        }

        return "purchase-orders";
    }
}

