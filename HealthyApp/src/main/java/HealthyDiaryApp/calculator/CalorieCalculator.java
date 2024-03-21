package HealthyDiaryApp.calculator;

import HealthyDiaryApp.enums.ActivityLevel;

/*
 * CalorieCalculator class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: В цьому класі робляться підрахунки для визначення калоражу, норми поживних речовин;
 */


public class CalorieCalculator {
    private static final double MALE_CONSTANT_A = 66.5;
    private static final double MALE_CONSTANT_B = 13.75;
    private static final double MALE_CONSTANT_C = 5.003;
    private static final double MALE_CONSTANT_D = 6.775;

    private static final double FEMALE_CONSTANT_A = 655.1;
    private static final double FEMALE_CONSTANT_B = 9.563;
    private static final double FEMALE_CONSTANT_C = 1.85;
    private static final double FEMALE_CONSTANT_D = 4.676;

    private static final double PROTEIN_CALORIES_PERCENTAGE = 0.20; // 30%
    private static final double FAT_CALORIES_PERCENTAGE = 0.30;     // 30%
    private static final double CARBS_CALORIES_PERCENTAGE = 0.40;   // 40%

    private static final double BREAKFAST_PERCENTAGE = 0.25;
    private static final double FIRST_SNACK_PERCENTAGE = 0.12;
    private static final double LUNCH_PERCENTAGE = 0.3;
    private static final double DINNER_PERCENTAGE = 0.2;

    private static final double LOW_LEVEL_ACTIVITY = 1.3;
    private static final double MIDDLE_LEVEL_ACTIVITY = 1.5;
    private static final double HIGH_LEVEL_ACTIVITY = 1.7;
    private static final double DEFAULT_LEVEL_ACTIVITY = 1.0;

    private static final double CALORIES_FOR_ONE_GRAM_PROTEIN_CARBS = 4.1;
    private static final double CALORIES_FOR_ONE_GRAM_FAT = 7.3;

    public static double calculateCalories(double weight, double height, int age,
                                           boolean isMale, ActivityLevel activityLevel) {
        double bmr;

        if (isMale) {
            bmr = MALE_CONSTANT_A + (MALE_CONSTANT_B * weight) +
                    (MALE_CONSTANT_C * height) - (MALE_CONSTANT_D * age);
        } else {
            bmr = FEMALE_CONSTANT_A + (FEMALE_CONSTANT_B * weight) +
                    (FEMALE_CONSTANT_C * height) - (FEMALE_CONSTANT_D * age);
        }

        double calories = bmr * getActivityMultiplier(activityLevel);
        return Math.round(calories * 10.0) / 10.0;
    }

    private static double getActivityMultiplier(ActivityLevel activityLevel) {
        switch (activityLevel) {
            case Low:
                return LOW_LEVEL_ACTIVITY;
            case Medium:
                return MIDDLE_LEVEL_ACTIVITY;
            case High:
                return HIGH_LEVEL_ACTIVITY;
            default:
                return DEFAULT_LEVEL_ACTIVITY;
        }
    }

    //Метод для розрахунку кількості білків на день
    public static double calculateProtein(double totalCalories) {
        return Math.round(((totalCalories * PROTEIN_CALORIES_PERCENTAGE) / CALORIES_FOR_ONE_GRAM_PROTEIN_CARBS) * 10.0)/ 10.0; // 1 г белка = 4.1 калории
    }

    // Метод для розрахунку кількості жирів на день
    public static double calculateFat(double totalCalories) {
        return Math.round(((totalCalories * FAT_CALORIES_PERCENTAGE) / CALORIES_FOR_ONE_GRAM_FAT) * 10.0)/ 10.0; // 1 г жира = 7.3 калорий
    }

    // Метод розрахунку кількості вуглеводів на день
    public static double calculateCarbs(double totalCalories) {
        return Math.round(((totalCalories * CARBS_CALORIES_PERCENTAGE) / CALORIES_FOR_ONE_GRAM_PROTEIN_CARBS) * 10.0)/ 10.0; // 1 г углеводов = 4.1 калории
    }

    //Метод для розрахунку каларажу для сніданку
        public static double numberOfGramsForBreakfast(double totalCalories){
            return Math.round((totalCalories * BREAKFAST_PERCENTAGE));
    }

    //Метод для розрахунку каларажу для обіду
    public static double numberOfGramsForLunch(double totalCalories){
        return Math.round((totalCalories * LUNCH_PERCENTAGE));
    }

    //Метод для розрахунку каларажу для вечері
    public static double numberOfGramsForDinner(double totalCalories){
        return Math.round((totalCalories * DINNER_PERCENTAGE));
    }

    //Метод для розрахунку каларажу для вечері
    public static double numberOfGramsForSnack(double totalCalories){
        return Math.round((totalCalories * FIRST_SNACK_PERCENTAGE));
    }
}
