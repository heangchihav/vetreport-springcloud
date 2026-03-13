package com.example.branchreportservice.api.producttype;

import com.example.branchreportservice.dto.ProductTypeRequest;
import com.example.branchreportservice.dto.ProductTypeResponse;
import com.example.branchreportservice.service.ProductTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/branchreport/product-types")
public class ProductTypeController {

    private final ProductTypeService productTypeService;

    public ProductTypeController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @PostMapping
    public ResponseEntity<ProductTypeResponse> createProductType(@Valid @RequestBody ProductTypeRequest request,
            @RequestParam Long productId) {
        ProductTypeResponse response = productTypeService.createProductType(request, productId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductTypeResponse> updateProductType(@PathVariable Long id,
            @Valid @RequestBody ProductTypeRequest request) {
        ProductTypeResponse response = productTypeService.updateProductType(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductTypeResponse> getProductTypeById(@PathVariable Long id) {
        ProductTypeResponse response = productTypeService.getProductTypeById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductTypeResponse>> getAllProductTypes() {
        List<ProductTypeResponse> response = productTypeService.getAllProductTypes();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductTypeResponse>> getProductTypesByProductId(@PathVariable Long productId) {
        List<ProductTypeResponse> response = productTypeService.getProductTypesByProductId(productId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}/active")
    public ResponseEntity<List<ProductTypeResponse>> getActiveProductTypesByProductId(@PathVariable Long productId) {
        List<ProductTypeResponse> response = productTypeService.getActiveProductTypesByProductId(productId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}/search")
    public ResponseEntity<List<ProductTypeResponse>> searchProductTypes(
            @PathVariable Long productId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean active) {

        List<ProductTypeResponse> response = productTypeService.searchProductTypes(productId, search, active);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<ProductTypeResponse>> getProductTypesByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {

        List<ProductTypeResponse> response = productTypeService.getProductTypesByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductTypeResponse>> getLowStockItems() {
        List<ProductTypeResponse> response = productTypeService.getLowStockItems();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductType(@PathVariable Long id) {
        productTypeService.deleteProductType(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ProductTypeResponse> deactivateProductType(@PathVariable Long id) {
        ProductTypeResponse response = productTypeService.deactivateProductType(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductTypeResponse> updateStock(@PathVariable Long id, @RequestParam Integer quantity) {
        ProductTypeResponse response = productTypeService.updateStock(id, quantity);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/stock/adjust")
    public ResponseEntity<ProductTypeResponse> adjustStock(@PathVariable Long id, @RequestParam Integer adjustment) {
        ProductTypeResponse response = productTypeService.adjustStock(id, adjustment);
        return ResponseEntity.ok(response);
    }
}
