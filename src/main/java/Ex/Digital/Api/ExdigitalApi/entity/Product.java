package Ex.Digital.Api.ExdigitalApi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Product", schema = "public")  
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product")
    private Integer idProduct;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "value", nullable = false) 
    private Integer value;
    
    @ManyToOne
    @JoinColumn(name = "id_category", nullable = false)
    private Category category;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "stock", nullable = false)
    private Integer stock;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @ManyToOne
    @JoinColumn(name = "id_discount")
    private Discount discount;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImages> images;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}