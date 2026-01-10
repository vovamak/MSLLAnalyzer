package com.aflt.product.repository;

import com.aflt.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p FROM Product p JOIN p.parts pp WHERE pp.partNumber = :partNumber")
    Product findByPartNumber(@Param("partNumber") String partNumber);

    @Query("SELECT p FROM Product p WHERE :listName MEMBER OF p.lists")
    List<Product> findByListName(@Param("listName") String listName);

    @Query("SELECT p FROM Product p WHERE p.productCode LIKE %:query% OR p.description LIKE %:query%")
    List<Product> searchByQuery(@Param("query") String query);
}