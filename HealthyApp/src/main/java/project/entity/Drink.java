package project.entity;

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
@Table(name = "Drinks")
public class Drink{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_drink")
    private Integer idDrink;

    @Column(name = "name_drink", nullable = false, length = 100)
    private String nameDrink;

    @Column(name = "calories",nullable = false)
    private Double caloriesDrink;

    @Column(name = "fat",nullable = false)
    private Double fatDrink;

    @Column(name = "protein",nullable = false)
    private Double proteinDrink;

    @Column(name = "carbs",nullable = false)
    private Double carbsDrink;
}
