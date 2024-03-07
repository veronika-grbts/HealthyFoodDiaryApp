package HealthyDiaryApp.entity;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import HealthyDiaryApp.enums.ActivityLevel;

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
    private String nameUser;

    @Column(name = "age", nullable = false)
    private int ageUser;

    @Column(name = "weight", nullable = false)
    private double weightUser;

    @Column(name = "height", nullable = false)
    private double heightUser;

    @Column(name = "gender", nullable = false, length = 50)
    private boolean genderUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_level", columnDefinition = "activity_level", nullable = false)
    @Type(type = "ActivityLevel")
    private ActivityLevel activityLevel;

    @Column(name = "allergies", nullable = false)
    private boolean allergiesUser;

    @Column(name = "cause", nullable = false)
    private boolean causeUser;

    @Column(name = "total_caloric", nullable = false)
    private double totalCaloricUser;

    @Column(name = "total_protein", nullable = false)
    private double totalProteinUser;

    @Column(name = "total_fat", nullable = false)
    private double totalFatUser;

    @Column(name = "total_carbohydrates", nullable = false)
    private double totalCarbsUser;

}
