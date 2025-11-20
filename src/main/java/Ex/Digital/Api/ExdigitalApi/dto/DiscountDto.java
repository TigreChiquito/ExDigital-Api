package Ex.Digital.Api.ExdigitalApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDto {
    private Integer idDiscount;
    private String name;
    private Boolean active;
    private BigDecimal discount;
    private LocalDate initDate;
    private LocalDate endDate;
    private String description;
}