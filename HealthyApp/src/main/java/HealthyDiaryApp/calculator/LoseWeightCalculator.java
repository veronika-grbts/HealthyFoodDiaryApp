/*
 * LoseWeightCalculator class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: В цьому класі робляться підрахунки для визначення калоражного дефіциту при схуднені,
 * ІМТ, калораж з урахуванням дефіциту;
 */
package HealthyDiaryApp.calculator;

import HealthyDiaryApp.enums.ActivityLevel;




public class LoseWeightCalculator {
    private static final double MALE_CONSTANT_A = 50;
    private static final double MALE_CONSTANT_B = 0.75;
    private static final double FEMALE_CONSTANT_A = 45.5;
    private static final double FEMALE_CONSTANT_B = 0.67;
    private static final double GENERAL_CONSTANT = 150;

    private static final double BMI_25_30_PERCENT = 0.2;
    private static final double BMI_30_35_PERCENT = 0.25;
    private static final double BMI_35_40_PERCENT = 0.3;
    private static final double BMI_40_PLUS_PERCENT = 0.4;
    private static final double DEFAULT_PERCENT = 0.0;
    private static final double BMI_18_5_24_9_PERCENT = 0.1;
    private static final double OPTIMAL_BMI = 19.0;

    private static final double NUMBER_OF_FAT_PER_KILOGRAM = 7700;

    private static final double LOWER_LIMIT_OF_NORMAL_BMI = 18.5;
    private static final double HIGH_LIMIT_OF_NORMAL_BMI = 24.9;

    private static final double LOWER_LIMIT_OF_EXCESSIVE_BMI = 25;
    private static final double HIGH_LIMIT_OF_EXCESSIVE_BMI = 29.9;

    private static final double LOWER_LIMIT_OF_OBESITY_ONE_BMI = 30;
    private static final double HIGH_LIMIT_OF_OBESITY_ONE_BMI = 34.9;

    private static final double LOWER_LIMIT_OF_OBESITY_SECOND_BMI = 35;
    private static final double HIGH_LIMIT_OF_OBESITY_SECOND_BMI = 39.9;

    private static final double LOWER_LIMIT_OF_OBESITY_THIRD_BMI = 35;
    // Метод для расчета индекса массы тела (ИМТ)
    public static double calculateBMI(double weight, double height) {
        double heightInM = height / 100.0;
        double bmi = weight / (heightInM * heightInM);
        return Math.round(bmi * 10.0) / 10.0;
    }

    //Метод для расчета лучшего веса для пользователя
    public static double calculateBestWeight(double height, boolean isMale) {
        double bmr;
        if (isMale) {
            bmr = MALE_CONSTANT_A + MALE_CONSTANT_B * (height - GENERAL_CONSTANT);
        } else {
            bmr = FEMALE_CONSTANT_A + FEMALE_CONSTANT_B * (height - GENERAL_CONSTANT);
        }
        double currentBMI = calculateBMI(bmr, height);
        if (currentBMI < LOWER_LIMIT_OF_NORMAL_BMI || currentBMI > HIGH_LIMIT_OF_NORMAL_BMI) {
            double newWeight = OPTIMAL_BMI * ((height / 100.0) * (height / 100.0));
            return Math.round(newWeight * 10.0) / 10.0;
        }
        return Math.round(bmr * 10.0) / 10.0;
    }

    public static double calculatorDeficitCaloric(double weight, double height,
        ActivityLevel activityLevel, int age, boolean isMale) {

        // Рассчитываем калорийность по методу calculateCalories из класса CalorieCalculator
        double totalCalories = CalorieCalculator.calculateCalories(weight,
                height, age, isMale,  activityLevel);

        // Рассчитываем ИМТ
        double bmi = calculateBMI(weight, height);

        // Определяем процентный коэффициент в зависимости от ИМТ
        double deficitPercent = DEFAULT_PERCENT;

        if ((bmi >= LOWER_LIMIT_OF_NORMAL_BMI) && (bmi <= HIGH_LIMIT_OF_NORMAL_BMI)) {
            deficitPercent = BMI_18_5_24_9_PERCENT;
        }else if ((bmi >=  LOWER_LIMIT_OF_EXCESSIVE_BMI) && (bmi < HIGH_LIMIT_OF_EXCESSIVE_BMI)) {
            deficitPercent = BMI_25_30_PERCENT;
        } else if ((bmi >= LOWER_LIMIT_OF_OBESITY_ONE_BMI ) && (bmi < HIGH_LIMIT_OF_OBESITY_ONE_BMI)) {
            deficitPercent = BMI_30_35_PERCENT;
        } else if ((bmi >=  LOWER_LIMIT_OF_OBESITY_SECOND_BMI) && (bmi <  HIGH_LIMIT_OF_OBESITY_SECOND_BMI)) {
            deficitPercent = BMI_35_40_PERCENT;
        } else if (bmi >= LOWER_LIMIT_OF_OBESITY_THIRD_BMI) {
            deficitPercent = BMI_40_PLUS_PERCENT;
        }

        // Вычисляем желаемый дефицит калорий
        double deficitCaloric = totalCalories * deficitPercent;
        return Math.round(deficitCaloric * 10.0) / 10.0;
    }

    public static  double caloriesDayWithDeficit(double weight, double height,
                                                 ActivityLevel activityLevel,
                                                 int age, boolean isMale){
        double calories = CalorieCalculator.calculateCalories(weight, height,
                age, isMale,  activityLevel);
        double deficit = LoseWeightCalculator.calculatorDeficitCaloric(weight,
                height,activityLevel,age, isMale);
        return  calories - deficit;
    }

    //Узнаем количество дней которое займет похудение
    public static short estimateTimeToReachGoal(double currentWeight,
                                                double targetWeight, double caloricDeficit) {
        // Количество калорий, которые нужно сжечь для достижения целевого веса
        double totalCaloriesToLose = (currentWeight - targetWeight) * NUMBER_OF_FAT_PER_KILOGRAM;
        // Количество дней, необходимых для достижения цели
        double daysToReachWeight = totalCaloriesToLose / caloricDeficit;
        return (short) daysToReachWeight;
    }
}
