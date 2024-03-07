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
@Table(name = "UserAllergy")
public class UserAllergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idUserAllergy;

    @ManyToOne
    @JoinColumn(name = "number_phone", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_ingredients", nullable = false)
    private Ingredients ingredients;

}
