package Ex.Digital.Api.ExdigitalApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Integer idOrder;
    private String orderNumber;
    private Integer userId;
    private String userName;
    private BigDecimal total;
    private String status;
    private String paymentMethod;
    private String shippingAddress;
    private String notes;
    private LocalDateTime createdAt;
    private List<OrderItemDto> items;
}