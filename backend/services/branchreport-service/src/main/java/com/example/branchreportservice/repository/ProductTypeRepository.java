package com.example.branchreportservice.repository;

import com.example.branchreportservice.entity.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {

        List<ProductType> findByProductId(Long productId);

        List<ProductType> findByProductIdAndActive(Long productId, Boolean active);

        Page<ProductType> findByProductIdAndActive(Long productId, Boolean active, Pageable pageable);

        List<ProductType> findByActive(Boolean active);

        List<ProductType> findBySizeCode(String sizeCode);

        @Query("SELECT pt FROM ProductType pt WHERE pt.name LIKE %:name%")
        Page<ProductType> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

        @Query("SELECT pt FROM ProductType pt WHERE pt.product.id = :productId AND (pt.name LIKE %:search% OR pt.sizeCode LIKE %:search%) AND pt.active = :active")
        Page<ProductType> searchProductTypes(@Param("productId") Long productId, @Param("search") String search,
                        @Param("active") Boolean active, Pageable pageable);

        @Query("SELECT pt FROM ProductType pt WHERE pt.price BETWEEN :minPrice AND :maxPrice AND pt.active = :active")
        Page<ProductType> findByPriceRange(@Param("minPrice") BigDecimal minPrice,
                        @Param("maxPrice") BigDecimal maxPrice,
                        @Param("active") Boolean active, Pageable pageable);

        @Query("SELECT pt FROM ProductType pt WHERE pt.price BETWEEN :minPrice AND :maxPrice AND pt.active = :active")
        List<ProductType> findByPriceRange(@Param("minPrice") BigDecimal minPrice,
                        @Param("maxPrice") BigDecimal maxPrice,
                        @Param("active") Boolean active);

        @Query("SELECT pt FROM ProductType pt WHERE pt.stockQuantity <= pt.minStockLevel AND pt.active = true")
        List<ProductType> findLowStockItems();

        @Query("SELECT COUNT(pt) FROM ProductType pt WHERE pt.product.id = :productId AND pt.active = true")
        long countActiveByProductId(@Param("productId") Long productId);

        boolean existsByNameAndProductId(String name, Long productId);

        boolean existsBySizeCodeAndProductId(String sizeCode, Long productId);
}
