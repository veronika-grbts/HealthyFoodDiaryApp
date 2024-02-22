package entity;

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
@Table(name = "UserSelectedMenu")
public class UserSelectedMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idSelectedMenu;

    @ManyToOne
    @JoinColumn(name = "phone_number", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "breakfast_id")
    private MealOption breakfast;

    @ManyToOne
    @JoinColumn(name = "breakfast_drink_id")
    private Drink breakfastDrink;

    @ManyToOne
    @JoinColumn(name = "lunch_id")
    private MealOption lunch;

    @ManyToOne
    @JoinColumn(name = "lunch_drink_id")
    private Drink lunchDrink;

    @ManyToOne
    @JoinColumn(name = "dinner_id")
    private MealOption dinner;

    @ManyToOne
    @JoinColumn(name = "dinner_drink_id")
    private Drink dinnerDrink;

    @Column(name = "calorie_breakfast", nullable = false)
    private Double gramsForBreakfastSelectedMenu;

    @Column(name = "calorie_lunch", nullable = false)
    private Double gramsForLunchSelectedMenu;

    @Column(name = "calorie_dinner", nullable = false)
    private Double gramsForDinnerSelectedMenu;
}
