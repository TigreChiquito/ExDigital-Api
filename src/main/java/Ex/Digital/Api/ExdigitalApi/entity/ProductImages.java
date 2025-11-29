package Ex.Digital.Api.ExdigitalApi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "product_images", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImages {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    @JsonIgnore
    private Product product;
    
    @Column(name = "url", nullable = false)
    private String url;
    
    @Column(name = "order_position", nullable = false)
    @Min(value = 1, message = "La posición debe ser mayor a 0")  
    private Integer orderPosition;
    
    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}