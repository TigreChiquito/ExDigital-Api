package Ex.Digital.Api.ExdigitalApi.controller;

import Ex.Digital.Api.ExdigitalApi.dto.ApiResponse;
import Ex.Digital.Api.ExdigitalApi.dto.CreateDiscountRequest;
import Ex.Digital.Api.ExdigitalApi.dto.DiscountDto;
import Ex.Digital.Api.ExdigitalApi.service.DiscountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@CrossOrigin(origins = "*")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<DiscountDto>>> getAllDiscounts() {
        try {
            List<DiscountDto> discounts = discountService.getAllDiscounts();
            return ResponseEntity.ok(ApiResponse.success("Descuentos obtenidos exitosamente", discounts));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<DiscountDto>>> getActiveDiscounts() {
        try {
            List<DiscountDto> discounts = discountService.getActiveDiscounts();
            return ResponseEntity.ok(ApiResponse.success("Descuentos activos obtenidos exitosamente", discounts));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DiscountDto>> getDiscountById(@PathVariable Integer id) {
        try {
            DiscountDto discount = discountService.getDiscountById(id);
            return ResponseEntity.ok(ApiResponse.success("Descuento obtenido exitosamente", discount));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DiscountDto>> createDiscount(@Valid @RequestBody CreateDiscountRequest request) {
        try {
            DiscountDto discount = discountService.createDiscount(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Descuento creado exitosamente", discount));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DiscountDto>> updateDiscount(
            @PathVariable Integer id,
            @Valid @RequestBody CreateDiscountRequest request) {
        try {
            DiscountDto discount = discountService.updateDiscount(id, request);
            return ResponseEntity.ok(ApiResponse.success("Descuento actualizado exitosamente", discount));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> activateDiscount(@PathVariable Integer id) {
        try {
            discountService.activateDiscount(id);
            return ResponseEntity.ok(ApiResponse.success("Descuento activado exitosamente", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deactivateDiscount(@PathVariable Integer id) {
        try {
            discountService.deactivateDiscount(id);
            return ResponseEntity.ok(ApiResponse.success("Descuento desactivado exitosamente", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}