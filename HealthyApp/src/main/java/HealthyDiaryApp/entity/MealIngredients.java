/*
 * MealIngredients  class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description:Клас, що представляє сутність зв'язків інгрідіентів для блюда  в програмі HealthyDiary.
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
@Builder
@Entity
@Table(name = "MealIngredients")
public class MealIngredients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mi")
    private Integer idMealIngredients;

    @ManyToOne
    @JoinColumn(name = "meal_id", nullable = false)
    private MealOption mealOption;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredients ingredients;

}
