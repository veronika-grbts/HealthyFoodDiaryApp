package Method;

import entity.ActivityLevel;


public class CalorieCalculator {

    private static final double MALE_CONSTANT_A = 66.5;
    private static final double MALE_CONSTANT_B = 13.75;
    private static final double MALE_CONSTANT_C = 5.003;
    private static final double MALE_CONSTANT_D = 6.775;

    private static final double FEMALE_CONSTANT_A = 655.1;
    private static final double FEMALE_CONSTANT_B = 9.563;
    private static final double FEMALE_CONSTANT_C = 1.85;
    private static final double FEMALE_CONSTANT_D = 4.676;

    private static final double PROTEIN_CALORIES_PERCENTAGE = 0.2; // 20%
    private static final double FAT_CALORIES_PERCENTAGE = 0.3;     // 30%
    private static final double CARBS_CALORIES_PERCENTAGE = 0.5;   // 50%

    private static final double BREAKFAST_PERCENTAGE = 0.25;
    private static final double LUNCH_PERCENTAGE = 0.3;
    private static final double DINNER_PERCENTAGE = 0.2;



    public static double calculateCalories(double weight, double height, int age, boolean isMale, ActivityLevel activityLevel) {
        double bmr;

        if (isMale) {
            bmr = MALE_CONSTANT_A + (MALE_CONSTANT_B * weight) + (MALE_CONSTANT_C * height) - (MALE_CONSTANT_D * age);
        } else {
            bmr = FEMALE_CONSTANT_A + (FEMALE_CONSTANT_B * weight) + (FEMALE_CONSTANT_C * height) - (FEMALE_CONSTANT_D * age);
        }

        double calories = bmr * getActivityMultiplier(activityLevel);

        // Округление до одного знака после запятой
        return Math.round(calories * 10.0) / 10.0;
    }

    private static double getActivityMultiplier(ActivityLevel activityLevel) {
        switch (activityLevel) {
            case Low:
                return 1.3;
            case Medium:
                return 1.5;
            case High:
                return 1.7;
            default:
                return 1.0;
        }
    }


    // Метод для расчета количества белков на день
    public static double calculateProtein(double totalCalories) {
        return Math.round(((totalCalories * PROTEIN_CALORIES_PERCENTAGE) / 4.1) * 10.0)/ 10.0; // 1 г белка = 4.1 калории
    }

    // Метод для расчета количества жиров на день
    public static double calculateFat(double totalCalories) {
        return Math.round(((totalCalories * FAT_CALORIES_PERCENTAGE) / 9.3) * 10.0)/ 10.0; // 1 г жира = 9.3 калорий
    }

    // Метод для расчета количества углеводов на день
    public static double calculateCarbs(double totalCalories) {
        return Math.round(((totalCalories * CARBS_CALORIES_PERCENTAGE) / 4.1) * 10.0)/ 10.0; // 1 г углеводов = 4.1 калории
    }

    //Метод для расчета калаража для завтрака
        public static double numberOfGramsForBreakfast(double totalCalories){
            return Math.round((totalCalories * BREAKFAST_PERCENTAGE));
    }

    //Метод для расчета калаража для обеда
    public static double numberOfGramsForLunch(double totalCalories){
        return Math.round((totalCalories * LUNCH_PERCENTAGE));
    }

    //Метод для расчета калаража для ужина
    public static double numberOfGramsForDinner(double totalCalories){
        return Math.round((totalCalories * DINNER_PERCENTAGE));
    }

    // Метод для расчета калорий с учетом желаемого снижения веса
    public static double calculateCaloriesLosingWeight(double totalCalorie) {
        // Отнимаем 350 калорий от общей суммы
        double result = totalCalorie - 350;
       if (result < 1200) {
            return 1200;
        } else {
            // Иначе возвращаем результат, учитывая снижение веса на 350 калорий
            return result;
        }
    }
}
