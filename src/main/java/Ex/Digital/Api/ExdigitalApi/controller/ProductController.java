package Ex.Digital.Api.ExdigitalApi.controller;

import Ex.Digital.Api.ExdigitalApi.dto.ApiResponse;
import Ex.Digital.Api.ExdigitalApi.dto.CreateProductRequest;
import Ex.Digital.Api.ExdigitalApi.dto.ProductDto;
import Ex.Digital.Api.ExdigitalApi.dto.UpdateProductRequest;
import Ex.Digital.Api.ExdigitalApi.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        try {
            List<ProductDto> products = productService.getAllProducts();
            return ResponseEntity.ok(ApiResponse.success("Productos obtenidos exitosamente", products));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getActiveProducts() {
        try {
            List<ProductDto> products = productService.getActiveProducts();
            return ResponseEntity.ok(ApiResponse.success("Productos activos obtenidos exitosamente", products));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAvailableProducts() {
        try {
            List<ProductDto> products = productService.getAvailableProducts();
            return ResponseEntity.ok(ApiResponse.success("Productos disponibles obtenidos exitosamente", products));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable Integer id) {
        try {
            ProductDto product = productService.getProductById(id);
            return ResponseEntity.ok(ApiResponse.success("Producto obtenido exitosamente", product));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProductsByCategory(@PathVariable Integer categoryId) {
        try {
            List<ProductDto> products = productService.getProductsByCategory(categoryId);
            return ResponseEntity.ok(ApiResponse.success("Productos por categoría obtenidos exitosamente", products));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductDto>>> searchProducts(@RequestParam String keyword) {
        try {
            List<ProductDto> products = productService.searchProducts(keyword);
            return ResponseEntity.ok(ApiResponse.success("Búsqueda completada exitosamente", products));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        try {
            ProductDto product = productService.createProduct(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Producto creado exitosamente", product));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateProductRequest request) {
        try {
            ProductDto product = productService.updateProduct(id, request);
            return ResponseEntity.ok(ApiResponse.success("Producto actualizado exitosamente", product));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Integer id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(ApiResponse.success("Producto eliminado exitosamente", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateStock(
            @PathVariable Integer id,
            @RequestParam Integer quantity) {
        try {
            productService.updateStock(id, quantity);
            return ResponseEntity.ok(ApiResponse.success("Stock actualizado exitosamente", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}