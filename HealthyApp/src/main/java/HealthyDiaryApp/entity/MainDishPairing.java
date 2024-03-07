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
@Table(name = "MainDishPairing")
public class MainDishPairing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "main_dish_id")
    private MealOption mainDish;

    @ManyToOne
    @JoinColumn(name = "additional_dish_id")
    private MealOption additionalDish;

}