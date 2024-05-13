package HealthyDiaryApp.calculator;

public abstract class AbstractCalculator implements Calculator {
    // Общие константы
    protected static final double CALORIES_FOR_ONE_GRAM_PROTEIN_CARBS = 4.1;
    protected static final double CALORIES_FOR_ONE_GRAM_FAT = 7.3;
    protected static final double NUMBER_OF_FAT_PER_KILOGRAM = 7700;
    private static final double DEFAULT_PERCENT = 0.0;

    // Метод для расчета калорий на основе основных параметров
    protected double calculateBasicCalories(double weight, double height, int age, boolean isMale,
                                            double a, double b, double c, double d) {
        double bmr;
        if (isMale) {
            bmr = a + (b * weight) + (c * height) - (d * age);
        } else {
            bmr = a + (b * weight) + (c * height) - (d * age);
        }
        return bmr;
    }

    // Метод для расчета дневного дефицита калорий
    protected double calculateDeficitCaloric(double totalCalories, double bmi, double[] bmiCategories) {
        double deficitPercent = DEFAULT_PERCENT;
        for (int i = 0; i < bmiCategories.length; i++) {
            if (bmi >= bmiCategories[i] && bmi < bmiCategories[i + 1]) {
                deficitPercent = bmiCategories[i + 2];
                break;
            }
            i += 2;
        }
        double deficitCaloric = totalCalories * deficitPercent;
        return Math.round(deficitCaloric * 10.0) / 10.0;
    }
}
