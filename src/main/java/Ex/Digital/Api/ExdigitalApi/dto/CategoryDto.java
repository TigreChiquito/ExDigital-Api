package Ex.Digital.Api.ExdigitalApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Integer idCategories;
    private String name;
    private String description;
}