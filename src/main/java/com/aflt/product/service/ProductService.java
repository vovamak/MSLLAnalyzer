package com.aflt.product.service;

import com.aflt.product.entity.Product;
import com.aflt.product.entity.PartNumber;
import com.aflt.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Получить все продукты
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Найти продукт по ID
     */
    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow(() ->
                new RuntimeException("Product not found with id: " + id));
    }

    /**
     * Найти продукт по коду
     */
    public Product getProductByCode(String productCode) {
        return productRepository.findAll().stream()
                .filter(p -> p.getProductCode().equalsIgnoreCase(productCode))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Product not found with code: " + productCode));
    }

    /**
     * Найти продукт по part number
     */
    public Product getProductByPartNumber(String partNumber) {
        return productRepository.findByPartNumber(partNumber);
    }

    /**
     * Поиск продуктов по запросу
     */
    public List<Product> searchProducts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return productRepository.searchByQuery(query.trim());
    }

    /**
     * Сохранить или обновить продукт
     */
    @Transactional
    public Product saveProduct(Product product) {
        // Проверка уникальности кода продукта при создании нового
        if (product.getId() == null) {
            checkProductCodeUniqueness(product.getProductCode());
        } else {
            // При обновлении проверяем, что код не изменился на уже существующий
            Product existingProduct = getProductById(product.getId());
            if (!existingProduct.getProductCode().equals(product.getProductCode())) {
                checkProductCodeUniqueness(product.getProductCode());
            }
        }

        return productRepository.save(product);
    }

    /**
     * Удалить продукт
     */
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    /**
     * Добавить часть к продукту
     */
    @Transactional
    public void addPartToProduct(Long productId, String partNumber,
                                 String measureUnit, Integer priority) {
        Product product = getProductById(productId);

        // Проверяем, существует ли уже такая часть
        boolean partExists = product.getParts().stream()
                .anyMatch(part -> part.getPartNumber().equalsIgnoreCase(partNumber));

        if (partExists) {
            throw new RuntimeException("Part number already exists in this product: " + partNumber);
        }

        // Проверяем, не используется ли этот part number в другом продукте
        Product existingProductWithPart = getProductByPartNumber(partNumber);
        if (existingProductWithPart != null &&
                !existingProductWithPart.getId().equals(productId)) {
            throw new RuntimeException("Part number " + partNumber +
                    " already belongs to product: " + existingProductWithPart.getProductCode());
        }

        PartNumber part = new PartNumber();
        part.setPartNumber(partNumber);
        part.setMeasureUnit(measureUnit);
        part.setPriority(priority != null ? priority : 1);

        product.addPart(part);
        productRepository.save(product);
    }

    /**
     * Удалить часть из продукта
     */
    @Transactional
    public void removePartFromProduct(Long productId, Long partId) {
        Product product = getProductById(productId);

        PartNumber partToRemove = product.getParts().stream()
                .filter(part -> part.getId().equals(partId))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Part not found with id: " + partId + " in product: " + productId));

        product.removePart(partToRemove);
        productRepository.save(product);
    }

    /**
     * Обновить приоритет части продукта
     */
    @Transactional
    public void updatePartPriority(Long productId, Long partId, Integer newPriority) {
        Product product = getProductById(productId);

        PartNumber part = product.getParts().stream()
                .filter(p -> p.getId().equals(partId))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Part not found with id: " + partId));

        part.setPriority(newPriority);
        productRepository.save(product);
    }

    /**
     * Добавить продукт в список
     */
    @Transactional
    public void addProductToList(Long productId, String listName) {
        Product product = getProductById(productId);

        List<String> validLists = List.of("MSLL", "НЗ", "общехоз", "STR");
        if (!validLists.contains(listName)) {
            throw new RuntimeException("Invalid list name. Valid values: " + validLists);
        }

        product.addToList(listName);
        productRepository.save(product);
    }

    /**
     * Удалить продукт из списка
     */
    @Transactional
    public void removeProductFromList(Long productId, String listName) {
        Product product = getProductById(productId);
        product.removeFromList(listName);
        productRepository.save(product);
    }

    /**
     * Получить продукты по списку
     */
    public List<Product> getProductsByList(String listName) {
        return productRepository.findByListName(listName);
    }

    /**
     * Получить все списки, в которых есть продукты
     */
    public List<String> getAllProductLists() {
        return productRepository.findAll().stream()
                .flatMap(product -> product.getLists().stream())
                .distinct()
                .sorted()
                .toList();
    }

    /**
     * Создать новый продукт с несколькими частями
     */
    @Transactional
    public Product createProductWithParts(String productCode, String description,
                                          String category, List<PartNumber> parts,
                                          List<String> lists) {
        checkProductCodeUniqueness(productCode);

        Product product = new Product(productCode, description, category);

        if (lists != null && !lists.isEmpty()) {
            product.setLists(lists);
        }

        if (parts != null && !parts.isEmpty()) {
            for (PartNumber part : parts) {
                // Проверяем, не используется ли part number в других продуктах
                Product existingProduct = getProductByPartNumber(part.getPartNumber());
                if (existingProduct != null) {
                    throw new RuntimeException("Part number " + part.getPartNumber() +
                            " already belongs to product: " + existingProduct.getProductCode());
                }
                product.addPart(part);
            }
        }

        return productRepository.save(product);
    }

    /**
     * Массовое обновление продуктов из Excel/CSV
     */
    @Transactional
    public List<Product> bulkUpdateProducts(List<Product> products) {
        List<Product> savedProducts = new ArrayList<>();

        for (Product product : products) {
            try {
                // Проверяем, существует ли продукт с таким кодом
                Product existingProduct = productRepository.findAll().stream()
                        .filter(p -> p.getProductCode().equalsIgnoreCase(product.getProductCode()))
                        .findFirst()
                        .orElse(null);

                if (existingProduct != null) {
                    // Обновляем существующий продукт
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setCategory(product.getCategory());
                    existingProduct.setLists(product.getLists());

                    // Обновляем части
                    if (product.getParts() != null) {
                        // Удаляем старые части
                        existingProduct.getParts().clear();

                        // Добавляем новые части
                        for (PartNumber part : product.getParts()) {
                            PartNumber newPart = new PartNumber();
                            newPart.setPartNumber(part.getPartNumber());
                            newPart.setMeasureUnit(part.getMeasureUnit());
                            newPart.setPriority(part.getPriority());
                            existingProduct.addPart(newPart);
                        }
                    }

                    savedProducts.add(productRepository.save(existingProduct));
                } else {
                    // Создаем новый продукт
                    savedProducts.add(productRepository.save(product));
                }
            } catch (Exception e) {
                throw new RuntimeException("Error updating product " +
                        product.getProductCode() + ": " + e.getMessage(), e);
            }
        }

        return savedProducts;
    }

    /**
     * Проверить, существует ли продукт с заданным кодом
     */
    public boolean productExistsByCode(String productCode) {
        return productRepository.findAll().stream()
                .anyMatch(p -> p.getProductCode().equalsIgnoreCase(productCode));
    }

    /**
     * Получить все part numbers всех продуктов
     */
    public List<String> getAllPartNumbers() {
        return productRepository.findAll().stream()
                .flatMap(product -> product.getParts().stream())
                .map(PartNumber::getPartNumber)
                .distinct()
                .sorted()
                .toList();
    }

    /**
     * Получить продукты без списков
     */
    public List<Product> getProductsWithoutLists() {
        return productRepository.findAll().stream()
                .filter(product -> product.getLists() == null || product.getLists().isEmpty())
                .toList();
    }

    /**
     * Проверка уникальности кода продукта
     */
    private void checkProductCodeUniqueness(String productCode) {
        if (productExistsByCode(productCode)) {
            throw new RuntimeException("Product with code '" + productCode + "' already exists");
        }
    }

    /**
     * Получить сводку по продуктам
     */
    public ProductSummary getProductSummary() {
        List<Product> allProducts = productRepository.findAll();

        ProductSummary summary = new ProductSummary();
        summary.setTotalProducts(allProducts.size());
        summary.setTotalParts(allProducts.stream()
                .mapToInt(p -> p.getParts().size())
                .sum());

        // Подсчет по спискам
        allProducts.forEach(product -> {
            product.getLists().forEach(list -> {
                summary.addToListCount(list);
            });
        });

        return summary;
    }

    /**
     * DTO для сводки по продуктам
     */
    public static class ProductSummary {
        private int totalProducts;
        private int totalParts;
        private java.util.Map<String, Integer> productsByList = new java.util.HashMap<>();

        public void addToListCount(String listName) {
            productsByList.put(listName, productsByList.getOrDefault(listName, 0) + 1);
        }

        // Геттеры и сеттеры
        public int getTotalProducts() { return totalProducts; }
        public void setTotalProducts(int totalProducts) { this.totalProducts = totalProducts; }
        public int getTotalParts() { return totalParts; }
        public void setTotalParts(int totalParts) { this.totalParts = totalParts; }
        public java.util.Map<String, Integer> getProductsByList() { return productsByList; }
        public void setProductsByList(java.util.Map<String, Integer> productsByList) {
            this.productsByList = productsByList;
        }
    }
}
