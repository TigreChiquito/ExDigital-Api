package Ex.Digital.Api.ExdigitalApi.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDiscountRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    
    @NotNull(message = "El descuento es obligatorio")
    @DecimalMin(value = "0.00", message = "El descuento no puede ser negativo")
    @DecimalMax(value = "100.00", message = "El descuento no puede ser mayor a 100")
    private BigDecimal discount;
    
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate initDate;
    
    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate endDate;
    
    private String description;
}