package com.aflt.order.repository;


import com.aflt.order.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findByPartNo(String partNo);

    List<PurchaseOrder> findByOrderNo(String orderNo);

    List<PurchaseOrder> findByTargetDateBefore(LocalDate date);

    @Query("SELECT p FROM PurchaseOrder p WHERE p.partNo IN :partNumbers")
    List<PurchaseOrder> findByPartNumbers(@Param("partNumbers") List<String> partNumbers);

    @Query("SELECT p FROM PurchaseOrder p WHERE p.backorder > 0")
    List<PurchaseOrder> findBackorders();

    @Query("SELECT DISTINCT p.partNo FROM PurchaseOrder p")
    List<String> findAllPartNumbers();

    @Query("SELECT p FROM PurchaseOrder p WHERE p.partNo LIKE %:searchTerm% OR p.orderNo LIKE %:searchTerm%")
    List<PurchaseOrder> search(@Param("searchTerm") String searchTerm);
}
