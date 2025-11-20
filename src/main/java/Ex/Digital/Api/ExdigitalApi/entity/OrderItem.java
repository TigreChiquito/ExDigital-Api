package Ex.Digital.Api.ExdigitalApi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Order_Items", schema = "public")  
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order_items")
    private Integer idOrderItems;
    
    @ManyToOne
    @JoinColumn(name = "id_order", nullable = false)
    @JsonIgnore
    private Order order;
    
    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private Product product;
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @Column(name = "price_at_purchase", nullable = false)
    private Integer priceAtPurchase;
    
    @Column(name = "discount_applied")
    private Integer discountApplied;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}