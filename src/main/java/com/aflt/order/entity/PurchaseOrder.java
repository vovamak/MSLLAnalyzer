package com.aflt.order.entity;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "purchase_order")
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", nullable = false, length = 50)
    private String orderNo;

    @Column(name = "ext_order_no", length = 100)
    private String extOrderNo;

    @Column(name = "order_date")
    @Temporal(TemporalType.DATE)
    private LocalDate date;

    @Column(name = "part_no", nullable = false, length = 100)
    private String partNo;

    @Column(name = "backorder")
    private Integer backorder;

    @Column(name = "shipment_no", length = 50)
    private String shipmentNo;

    @Column(name = "target_date")
    @Temporal(TemporalType.DATE)
    private LocalDate targetDate;

    @Column(name = "confirmed_date")
    @Temporal(TemporalType.DATE)
    private LocalDate confirmedDate;

    @Column(name = "priority", length = 20)
    private String priority;

    @Column(name = "responsible", length = 50)
    private String responsible;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public String getExtOrderNo() { return extOrderNo; }
    public void setExtOrderNo(String extOrderNo) { this.extOrderNo = extOrderNo; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getPartNo() { return partNo; }
    public void setPartNo(String partNo) { this.partNo = partNo; }

    public Integer getBackorder() { return backorder; }
    public void setBackorder(Integer backorder) { this.backorder = backorder; }

    public String getShipmentNo() { return shipmentNo; }
    public void setShipmentNo(String shipmentNo) { this.shipmentNo = shipmentNo; }

    public LocalDate getTargetDate() { return targetDate; }
    public void setTargetDate(LocalDate targetDate) { this.targetDate = targetDate; }

    public LocalDate getConfirmedDate() { return confirmedDate; }
    public void setConfirmedDate(LocalDate confirmedDate) { this.confirmedDate = confirmedDate; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getResponsible() { return responsible; }
    public void setResponsible(String responsible) { this.responsible = responsible; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    // Конструкторы
    public PurchaseOrder() {}

    public PurchaseOrder(String orderNo, String extOrderNo, LocalDate date,
                         String partNo, Integer backorder, String shipmentNo,
                         LocalDate targetDate, LocalDate confirmedDate,
                         String priority, String responsible, String createdBy) {
        this.orderNo = orderNo;
        this.extOrderNo = extOrderNo;
        this.date = date;
        this.partNo = partNo;
        this.backorder = backorder;
        this.shipmentNo = shipmentNo;
        this.targetDate = targetDate;
        this.confirmedDate = confirmedDate;
        this.priority = priority;
        this.responsible = responsible;
        this.createdBy = createdBy;
    }
}
