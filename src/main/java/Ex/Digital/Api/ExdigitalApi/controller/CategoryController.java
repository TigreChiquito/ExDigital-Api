package Ex.Digital.Api.ExdigitalApi.controller;

import Ex.Digital.Api.ExdigitalApi.dto.ApiResponse;
import Ex.Digital.Api.ExdigitalApi.dto.CategoryDto;
import Ex.Digital.Api.ExdigitalApi.dto.CreateCategoryRequest;
import Ex.Digital.Api.ExdigitalApi.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getAllCategories() {
        try {
            List<CategoryDto> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(ApiResponse.success("Categorías obtenidas exitosamente", categories));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> getCategoryById(@PathVariable Integer id) {
        try {
            CategoryDto category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(ApiResponse.success("Categoría obtenida exitosamente", category));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryDto>> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        try {
            CategoryDto category = categoryService.createCategory(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Categoría creada exitosamente", category));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryDto>> updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody CreateCategoryRequest request) {
        try {
            CategoryDto category = categoryService.updateCategory(id, request);
            return ResponseEntity.ok(ApiResponse.success("Categoría actualizada exitosamente", category));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Integer id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(ApiResponse.success("Categoría eliminada exitosamente", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}