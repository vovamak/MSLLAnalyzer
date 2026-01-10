package com.aflt.product.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_code", nullable = false, unique = true, length = 50)
    private String productCode;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "category", length = 50)
    private String category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartNumber> parts = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "product_lists", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "list_name")
    private List<String> lists = new ArrayList<>();

    // Конструкторы, геттеры, сеттеры
    public Product() {}

    public Product(String productCode, String description, String category) {
        this.productCode = productCode;
        this.description = description;
        this.category = category;
    }

    public void addPart(PartNumber part) {
        parts.add(part);
        part.setProduct(this);
    }

    public void removePart(PartNumber part) {
        parts.remove(part);
        part.setProduct(null);
    }

    public void addToList(String list) {
        if (!lists.contains(list)) {
            lists.add(list);
        }
    }

    public void removeFromList(String list) {
        lists.remove(list);
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public List<PartNumber> getParts() { return parts; }
    public void setParts(List<PartNumber> parts) { this.parts = parts; }
    public List<String> getLists() { return lists; }
    public void setLists(List<String> lists) { this.lists = lists; }
}

