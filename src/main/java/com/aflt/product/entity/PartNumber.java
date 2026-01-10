package com.aflt.product.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "part_number")
public class PartNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "part_number", nullable = false, length = 100)
    private String partNumber;

    @Column(name = "measure_unit", length = 20)
    private String measureUnit;

    @Column(name = "priority", nullable = false)
    private Integer priority = 1;

    // Конструкторы, геттеры, сеттеры
    public PartNumber() {}

    public PartNumber(String partNumber, String measureUnit) {
        this.partNumber = partNumber;
        this.measureUnit = measureUnit;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public String getPartNumber() { return partNumber; }
    public void setPartNumber(String partNumber) { this.partNumber = partNumber; }
    public String getMeasureUnit() { return measureUnit; }
    public void setMeasureUnit(String measureUnit) { this.measureUnit = measureUnit; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
}

