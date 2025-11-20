package Ex.Digital.Api.ExdigitalApi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    
    @NotNull(message = "El ID de usuario es obligatorio")
    private Integer userId;
    
    @NotEmpty(message = "Debe haber al menos un item en la orden")
    private List<OrderItemRequest> items;
    
    @NotBlank(message = "El método de pago es obligatorio")
    private String paymentMethod;
    
    @NotBlank(message = "La dirección de envío es obligatoria")
    private String shippingAddress;
    
    private String notes;
}