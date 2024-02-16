package entity;

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
@Table(name = "MealOption")
@TypeDef(name = "MealType", typeClass = PostgreSQLEnumType.class)
public class MealOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_option")
    private Integer idOption;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Double calories;
    @Column(nullable = false)
    private Double fat;
    @Column(nullable = false)
    private Double protein;
    @Column(nullable = false)
    private Double carbs;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_meal", columnDefinition = "type_meal", nullable = false)
    @Type(type = "MealType")
    private MealType mealType;

}
