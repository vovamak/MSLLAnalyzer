package com.aflt.store.entity;


import jakarta.persistence.*;

import java.util.Date;

@Table(name = "stock_aflt")
public class StockAFLT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "part_number", nullable = false)
    private String partNumber;

    @Column(name = "expire_date")
    @Temporal(TemporalType.DATE)
    private Date expireDate;

    @Column(nullable = false)
    private Double quantity;

    @Column(name = "measure_unit", length = 10)
    private String measureUnit;

    @Column(length = 10)
    private String station;

    @Column(length = 10)
    private String store;

    @Column(length = 50)
    private String owner;

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPartNumber() { return partNumber; }
    public void setPartNumber(String partNumber) { this.partNumber = partNumber; }
    public Date getExpireDate() { return expireDate; }
    public void setExpireDate(Date expireDate) { this.expireDate = expireDate; }
    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
    public String getMeasureUnit() { return measureUnit; }
    public void setMeasureUnit(String measureUnit) { this.measureUnit = measureUnit; }
    public String getStation() { return station; }
    public void setStation(String station) { this.station = station; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
}
