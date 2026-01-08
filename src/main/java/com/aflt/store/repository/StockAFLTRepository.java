package com.aflt.store.repository;

import com.aflt.store.entity.StockAFLT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockAFLTRepository extends JpaRepository<StockAFLT, Long> {
    List<StockAFLT> findByPartNumber(String partNumber);

}
