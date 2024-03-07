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
@Table(name = "WeightLossGoals")
public class WeightLossGoals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Integer idWeightLossGoals;

    @ManyToOne
    @JoinColumn(name = "phone_number", nullable = false)
    private User user;

    @Column(name = "current_weight", nullable = false)
    private double currentWeightUserLossGoals;

    @Column(name = "target_weight", nullable = false)
    private double targetWeightUserLossGoals;

    @Column(name = "target_caloric_deficit", nullable = false)
    private double targetCaloricDeficitUserLossGoals;

    @Column(name = "estimated_completion_time", nullable = false)
    private short estimatedCompletionTimeLossGoals;

    @Column(name = "calories_day_with_deficit", nullable = false)
    private double caloriesDayWithDeficitLossGoals;

}