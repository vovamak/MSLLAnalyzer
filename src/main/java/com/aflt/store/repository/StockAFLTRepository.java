package com.aflt.store.repository;

import com.aflt.store.entity.StockAFLT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockAFLTRepository extends JpaRepository<StockAFLT, Long> {

    List<StockAFLT> findByPartNumber(String partNumber);

    List<StockAFLT> findByOwner(String owner);

    List<StockAFLT> findByPartNumberContainingIgnoreCase(String partNumber);

    @Query("SELECT s FROM StockAFLT s WHERE s.partNumber IN :partNumbers")
    List<StockAFLT> findByPartNumbers(@Param("partNumbers") List<String> partNumbers);

    @Query("SELECT s FROM StockAFLT s WHERE s.quantity <= :minQuantity")
    List<StockAFLT> findLowStock(@Param("minQuantity") Double minQuantity);

    @Query("SELECT DISTINCT s.partNumber FROM StockAFLT s")
    List<String> findAllPartNumbers();

    @Query("SELECT s.owner, SUM(s.quantity) FROM StockAFLT s WHERE s.partNumber IN :partNumbers GROUP BY s.owner")
    List<Object[]> getStockSummaryByPartNumbers(@Param("partNumbers") List<String> partNumbers);
}
