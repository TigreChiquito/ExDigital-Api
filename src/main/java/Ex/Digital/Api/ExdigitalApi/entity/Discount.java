package Ex.Digital.Api.ExdigitalApi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Discount", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discount {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_discount")
    private Integer idDiscount;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "active", nullable = false)
    private Boolean active = true;
    
    @Column(name = "discount", nullable = false, precision = 10, scale = 2)
    private BigDecimal discount;
    
    @Column(name = "init_date")
    private LocalDate initDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}