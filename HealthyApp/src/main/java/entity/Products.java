package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Double calories;
    @Column(nullable = false)
    private Double fat;
    @Column(nullable = false)
    private Double protein;
    @Column(nullable = false)
    private Double carbs;
}
