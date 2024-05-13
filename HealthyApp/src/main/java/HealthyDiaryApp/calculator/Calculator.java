package HealthyDiaryApp.calculator;

import HealthyDiaryApp.enums.ActivityLevel;

public interface Calculator {
    double calculateCalories(double weight, double height, int age, boolean isMale, ActivityLevel activityLevel);
    double calculateBMI(double weight, double height);
}
