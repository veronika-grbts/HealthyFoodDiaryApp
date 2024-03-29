/*
 * WeightLossProgress  class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description:Клас, що представляє сутність прогресу  похудіння в програмі HealthyDiary.
 */

package HealthyDiaryApp.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "WeightLossProgress")
public class WeightLossProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Integer idWeightLossProgress;

    @ManyToOne
    @JoinColumn(name = "goal_id", nullable = false)
    private WeightLossGoals goal;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "current_weight", nullable = false)
    private Double currentWeight;

    @Column(name = "caloric_intake")
    private Double caloricIntake;

    @Column(name = "deficit_caloric")
    private Double deficitCaloric;

    @Column(name = "goal_achieved")
    private boolean goalAchieved;

}
