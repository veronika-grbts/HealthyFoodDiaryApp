/*
 * MealIngredientsDrink  class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description:Клас, що представляє сутність зв'язків інгрідіентів для напою  в програмі HealthyDiary.
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
@Table(name = "MealIngredientsDrink")
public class MealIngredientsDrink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ingredients_drink")
    private Integer idMealIngredientsDrink;

    @ManyToOne
    @JoinColumn(name = "id_drink", nullable = false)
    private Drink drink;

    @ManyToOne
    @JoinColumn(name = "id_ingredients", nullable = false)
    private Ingredients ingredients;
}


