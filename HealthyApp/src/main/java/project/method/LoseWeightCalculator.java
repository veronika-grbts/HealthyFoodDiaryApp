package project.method;

import project.entity.ActivityLevel;

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
        if (currentBMI < 18.5 || currentBMI > 24.9) {
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

        if (bmi >= 18.5 && bmi <= 24.9) {
            deficitPercent = BMI_18_5_24_9_PERCENT;
        }else if (bmi >= 25.0 && bmi < 30.0) {
            deficitPercent = BMI_25_30_PERCENT;
        } else if (bmi >= 30.0 && bmi < 35.0) {
            deficitPercent = BMI_30_35_PERCENT;
        } else if (bmi >= 35.0 && bmi < 40.0) {
            deficitPercent = BMI_35_40_PERCENT;
        } else if (bmi >= 40.0) {
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
        double totalCaloriesToLose = (currentWeight - targetWeight) * 7700;
        // Количество дней, необходимых для достижения цели
        double daysToReachWeight = totalCaloriesToLose / caloricDeficit;
        return (short) daysToReachWeight;
    }
}
