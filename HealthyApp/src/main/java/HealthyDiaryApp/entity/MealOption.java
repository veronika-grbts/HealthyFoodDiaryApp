/*
 * MealOption  class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description:Клас, що представляє сутність блюда для прийома їжі  в програмі HealthyDiary.
 */
package HealthyDiaryApp.entity;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import HealthyDiaryApp.enums.MealType;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "MealOption")
@TypeDef(name = "MealType", typeClass = PostgreSQLEnumType.class)
public class MealOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_option")
    private Integer idOption;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "calories",nullable = false)
    private Double caloriesMealOption;

    @Column(name = "fat",nullable = false)
    private Double fatMealOption;

    @Column(name = "protein",nullable = false)
    private Double proteinMealOption;

    @Column(name = "carbs",nullable = false)
    private Double carbsMealOption;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_meal", columnDefinition = "type_meal", nullable = false)
    @Type(type = "MealType")
    private MealType mealType;

    @Column(name = "additional_dish_id")
    private Integer additionalDishId;

}
