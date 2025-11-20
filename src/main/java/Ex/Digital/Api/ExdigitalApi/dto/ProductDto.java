package Ex.Digital.Api.ExdigitalApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Integer idProduct;
    private String name;
    private BigDecimal value;
    private String categoryName;
    private Integer categoryId;
    private String description;
    private Integer stock;
    private BigDecimal discount;
    private String discountName;
    private Boolean isActive;
    private List<String> imageUrls;
    private String primaryImage;
    private BigDecimal finalPrice;
}