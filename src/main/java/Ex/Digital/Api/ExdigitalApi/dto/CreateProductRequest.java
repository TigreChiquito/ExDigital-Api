package Ex.Digital.Api.ExdigitalApi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    
    @NotNull(message = "El valor es obligatorio")
    @Min(value = 1, message = "El valor debe ser mayor a 0")
    private Integer value; 
    
    @NotNull(message = "La categoría es obligatoria")
    private Integer categoryId;
    
    private String description;
    
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
    
    private Integer discountId;
}