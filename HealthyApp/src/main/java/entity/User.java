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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Users")
@TypeDef(name = "ActivityLevel", typeClass = PostgreSQLEnumType.class)
public class User {
    @Id
    @Column(name = "phone_number", nullable = false)
    private long phoneNumber;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "weight", nullable = false)
    private double weight;

    @Column(name = "height", nullable = false)
    private double height;

    @Column(name = "gender", nullable = false, length = 50)
    private boolean gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_level", columnDefinition = "activity_level", nullable = false)
    @Type(type = "ActivityLevel")
    private ActivityLevel activityLevel;

    @Column(name = "allergies", nullable = false)
    private boolean allergies;

    @Column(name = "cause", nullable = false)
    private boolean cause;

    @Column(name = "total_caloric", nullable = false)
    private double totalCaloric;

    @Column(name = "total_protein", nullable = false)
    private double totalProtein;

    @Column(name = "total_fat", nullable = false)
    private double totalFat;

    @Column(name = "total_carbohydrates", nullable = false)
    private double totalCarbs;

}
