package com.aflt.store.entity;


import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "stock_aflt")
public class StockAFLT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "part_number", nullable = false)
    private String partNumber;

    @Column(name = "expire_date")
    @Temporal(TemporalType.DATE)
    private LocalDate expireDate;

    @Column(name ="quantity", nullable = false)
    private Double quantity;

    @Column(name = "measure_unit", length = 10)
    private String measureUnit;

    @Column(name = "station", length = 10)
    private String station;

    @Column(name = "store", length = 10)
    private String store;

    @Column(name = "owner", length = 50)
    private String owner;

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPartNumber() { return partNumber; }
    public void setPartNumber(String partNumber) { this.partNumber = partNumber; }
    public LocalDate getExpireDate() { return expireDate; } // Изменено на LocalDate
    public void setExpireDate(LocalDate expireDate) { this.expireDate = expireDate; } // Изменено на LocalDate
    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
    public String getMeasureUnit() { return measureUnit; }
    public void setMeasureUnit(String measureUnit) { this.measureUnit = measureUnit; }
    public String getStation() { return station; }
    public void setStation(String station) { this.station = station; }
    public String getStore() { return store; }
    public void setStore(String store) { this.store = store; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    // Конструкторы
    public StockAFLT() {}

    public StockAFLT(String partNumber, LocalDate expireDate, Double quantity,
                     String measureUnit, String station, String owner, String store) {
        this.partNumber = partNumber;
        this.expireDate = expireDate;
        this.quantity = quantity;
        this.measureUnit = measureUnit;
        this.station = station;
        this.owner = owner;
        this.store = store;
    }
}
