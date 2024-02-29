package project.entity;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "MainDishPairing") // указываем имя таблицы в базе данных
public class MainDishPairing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "main_dish_id") // указываем имя столбца, который ссылается на основное блюдо
    private MealOption mainDish;

    @ManyToOne
    @JoinColumn(name = "additional_dish_id") // указываем имя столбца, который ссылается на дополнительное блюдо
    private MealOption additionalDish;

}