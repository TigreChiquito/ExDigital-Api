package Ex.Digital.Api.ExdigitalApi.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {
    
    private String name;
    
    @Min(value = 1, message = "El valor debe ser mayor a 0")
    private Integer value; // ⚠️ CAMBIO
    
    private Integer categoryId;
    
    private String description;
    
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
    
    private Integer discountId;
    
    private Boolean isActive;
}