package com.example.branchreportservice.repository;

import com.example.branchreportservice.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActive(Boolean active);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name% OR p.description LIKE %:name%")
    Page<Product> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name% OR p.description LIKE %:name%")
    List<Product> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE %:search% OR p.description LIKE %:search%) AND p.active = :active")
    Page<Product> searchProducts(@Param("search") String search, @Param("active") Boolean active, Pageable pageable);

    Page<Product> findByActive(Boolean active, Pageable pageable);
}
