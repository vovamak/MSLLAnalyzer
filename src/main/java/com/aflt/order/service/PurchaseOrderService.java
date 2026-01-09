package com.aflt.order.service;

import com.aflt.order.entity.PurchaseOrder;
import com.aflt.order.repository.PurchaseOrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    public void deletePurchaseOrder(Long id) {
        purchaseOrderRepository.deleteById(id);
    }

    public List<PurchaseOrder> getPurchaseOrdersByPartNo(String partNo) {
        return purchaseOrderRepository.findByPartNo(partNo);
    }

    public List<PurchaseOrder> getPurchaseOrdersByOrderNo(String orderNo) {
        return purchaseOrderRepository.findByOrderNo(orderNo);
    }

    @Transactional
    public void deleteAllPurchaseOrders() {
        purchaseOrderRepository.deleteAll();
        // Сбрасываем последовательность
        entityManager.createNativeQuery("ALTER SEQUENCE purchase_order_id_seq RESTART WITH 1").executeUpdate();
    }

    public void saveAllPurchaseOrders(List<PurchaseOrder> purchaseOrders) {
        purchaseOrderRepository.saveAll(purchaseOrders);
    }
}
