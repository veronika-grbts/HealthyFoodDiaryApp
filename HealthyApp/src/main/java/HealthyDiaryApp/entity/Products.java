/*
 * Products  class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description:Клас, що представляє сутність  продукта  в програмі HealthyDiary.
 */
package HealthyDiaryApp.entity;
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
    private String nameProduct;

    @Column(name = "calories", nullable = false)
    private Double caloriesProducts;

    @Column(name = "fat", nullable = false)
    private Double fatProducts;

    @Column(name = "protein", nullable = false)
    private Double proteinProducts;

    @Column(name = "carbs", nullable = false)
    private Double carbsProducts;
}
