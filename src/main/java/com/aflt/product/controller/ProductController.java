package com.aflt.product.controller;

import com.aflt.product.entity.Product;
import com.aflt.product.entity.PartNumber;
import com.aflt.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product-list";
    }

    @GetMapping("/new")
    public String showProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("availableLists", Arrays.asList("MSLL", "НЗ", "общехоз", "STR"));
        return "product-form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam(value = "lists", required = false) List<String> lists,
                              Model model) {
        if (lists != null) {
            product.setLists(lists);
        }

        productService.saveProduct(product);
        model.addAttribute("message", "Product saved successfully!");
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);

        // Используем List.of вместо Arrays.asList для Java 11+
        model.addAttribute("availableLists", java.util.Arrays.asList("MSLL", "НЗ", "общехоз", "STR"));

        return "product-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    @GetMapping("/search")
    public String searchProduct(@RequestParam(value = "query", required = false) String query,
                                Model model) {
        if (query != null && !query.trim().isEmpty()) {
            List<Product> products = productService.searchProducts(query);
            model.addAttribute("products", products);
            model.addAttribute("searchQuery", query);
        }
        return "product-search";
    }

    @PostMapping("/{productId}/add-part")
    public String addPart(@PathVariable Long productId,
                          @RequestParam String partNumber,
                          @RequestParam String measureUnit,
                          @RequestParam(defaultValue = "1") Integer priority) {
        productService.addPartToProduct(productId, partNumber, measureUnit, priority);
        return "redirect:/products/edit/" + productId;
    }

    @GetMapping("/{productId}/remove-part/{partId}")
    public String removePart(@PathVariable Long productId,
                             @PathVariable Long partId) {
        productService.removePartFromProduct(productId, partId);
        return "redirect:/products/edit/" + productId;
    }
}
