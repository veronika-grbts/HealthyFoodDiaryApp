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
@Table(name = "UserSelectedProduct")
public class UserSelectedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idUserSelectedProduct;

    @ManyToOne
    @JoinColumn(name = "phone_number", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Products products;

    @Column(name = "grams", nullable = false)
    private Double gramsUserSelectedProduct;

    @Column(name = "fat_remaining", nullable = false)
    private Double fatUserSelectedProduct;

    @Column(name = "protein_remaining", nullable = false)
    private Double proteinUserSelectedProduct;

    @Column(name = "carbs_remaining", nullable = false)
    private Double carbsUserSelectedProduct;

    @Column(name = "calories_remaining", nullable = false)
    private Double caloriesUserSelectedProduct;
}
