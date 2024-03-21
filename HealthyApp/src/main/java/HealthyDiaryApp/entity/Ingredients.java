/*
 * Ingredients  class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description:Клас, що представляє сутність інгрідієнтів в програмі HealthyDiary.
 */
package HealthyDiaryApp.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Ingredients")
public class Ingredients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ingredients")
    private Integer idIngredients;

    @Column(name = "name_ingredients", nullable = false, length = 50)
    private String nameIngredients;
}
