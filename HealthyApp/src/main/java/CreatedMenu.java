import Method.CalorieCalculator;
import Method.Nutrition;
import entity.*;
import util.HibernateMethods;

import java.util.List;
import java.util.Random;


public class CreatedMenu {
    private HibernateMethods hibernateMethods = new HibernateMethods();
    public void createMenu() {

        User user = getUserFromApplicationContext();
        boolean hasAllergy = userHasAllergy(user);

        if (!hasAllergy) {
            selectRandomMenu(user);
        } else {
            selectRandomMenuWithUserAllergy(user);
        }
    }

    private boolean userHasAllergy(User user) {
        return user.isAllergiesUser();
    }

    private void selectRandomMenu(User user) {
        List<MealOption> breakfastOptions = hibernateMethods.getAllMealOptionsByType(MealType.Breakfast);
        List<MealOption> lunchOptions = hibernateMethods.getAllMealOptionsByType(MealType.Lunch);
        List<MealOption> dinnerOptions = hibernateMethods.getAllMealOptionsByType(MealType.Dinner);

        MealOption breakfast = getRandomOption(breakfastOptions);
        MealOption lunch = getRandomOption(lunchOptions);
        MealOption dinner = getRandomOption(dinnerOptions);

        Drink breakfastDrink = getRandomDrink();
        Drink lunchDrink = getRandomDrink();
        Drink dinnerDrink = getRandomDrink();
        printSelectedMenuAndCalculateCalories(user, breakfast, lunch, dinner, breakfastDrink, lunchDrink, dinnerDrink);
    }


    private void selectRandomMenuWithUserAllergy(User user) {

        List<MealOption> breakfastOptions = hibernateMethods.getAllMealOptionsByType(MealType.Breakfast);
        List<MealOption> lunchOptions = hibernateMethods.getAllMealOptionsByType(MealType.Lunch);
        List<MealOption> dinnerOptions = hibernateMethods.getAllMealOptionsByType(MealType.Dinner);
        List<Drink> drinkOptions = hibernateMethods.getAllDrinks();

        MealOption breakfast = getRandomOptionWithAllergy(breakfastOptions, user);
        MealOption lunch = getRandomOptionWithAllergy(lunchOptions, user);
        MealOption dinner = getRandomOptionWithAllergy(dinnerOptions, user);

        Drink breakfastDrink = getRandomDrinkWithAllergy(drinkOptions, user);
        Drink lunchDrink = getRandomDrinkWithAllergy(drinkOptions, user);
        Drink dinnerDrink = getRandomDrinkWithAllergy(drinkOptions, user);
        printSelectedMenuAndCalculateCalories(user, breakfast, lunch, dinner, breakfastDrink, lunchDrink, dinnerDrink);
    }


    private void printSelectedMenuAndCalculateCalories(User user, MealOption breakfast, MealOption lunch, MealOption dinner, Drink breakfastDrink, Drink lunchDrink, Drink dinnerDrink) {
        //Розраховуємо загальний калораж для кожного прийому їжі
        double totalCalories = user.getTotalCaloricUser();
        double breakfastCalories = CalorieCalculator.numberOfGramsForBreakfast(totalCalories);
        double lunchCalories = CalorieCalculator.numberOfGramsForLunch(totalCalories);
        double dinnerCalories = CalorieCalculator.numberOfGramsForDinner(totalCalories);


        double breakfastDrinkCalories = breakfastDrink != null ? (breakfastDrink.getCaloriesDrink() * 2) : 0;
        double lunchDrinkCalories = lunchDrink != null ? (lunchDrink.getCaloriesDrink() * 2) : 0;
        double dinnerDrinkCalories = dinnerDrink != null ? (dinnerDrink.getCaloriesDrink() * 2) : 0;

        breakfastCalories -= breakfastDrinkCalories;
        lunchCalories -= lunchDrinkCalories;
        dinnerCalories -= dinnerDrinkCalories;

        // Отримуємо кількість грам для кожної страви
        double breakfastGrams = Math.round((getGramsFromCalories(breakfast.getCaloriesMealOption(), breakfastCalories) * 10.0) / 10.0);
        double lunchGrams = Math.round((getGramsFromCalories(lunch.getCaloriesMealOption(), lunchCalories) * 10.0) / 10.0);
        double dinnerGrams = Math.round((getGramsFromCalories(dinner.getCaloriesMealOption(), dinnerCalories) * 10.0) / 10.0);

        int breakfastId = hibernateMethods.findMealIdByName(breakfast.getName());
        int lunchId = hibernateMethods.findMealIdByName(lunch.getName());
        int dinnerId = hibernateMethods.findMealIdByName(dinner.getName());

        int breakfastDrinkId = hibernateMethods.findDrinkIdByName(breakfastDrink.getNameDrink());
        int lunchDrinkId = hibernateMethods.findDrinkIdByName(lunchDrink.getNameDrink());
        int dinnerDrinkId = hibernateMethods.findDrinkIdByName(dinnerDrink.getNameDrink());


        hibernateMethods.saveUserSelectedMenu(user.getPhoneNumber(), breakfastId,
                lunchId, dinnerId, breakfastDrinkId, lunchDrinkId, dinnerDrinkId,
                breakfastGrams, dinnerGrams, lunchGrams);
    }

    // Метод для вибору випадкової страви зі списку з огляду на алергії користувача
    private MealOption getRandomOptionWithAllergy(List<MealOption> options, User user) {
        List<UserAllergy> userAllergies = hibernateMethods.getUserAllergies(user);
        options.removeIf(option -> optionContainsAllergies(option, userAllergies));
        return getRandomOptionWithAllergy(options);
    }

    // Метод для вибору випадкового напою зі списку з огляду на алергію користувача
    private Drink getRandomDrinkWithAllergy(List<Drink> drinks, User user) {
        List<UserAllergy> userAllergies = hibernateMethods.getUserAllergies(user);
        drinks.removeIf(drink -> drinkContainsAllergies(drink, userAllergies));
        return getRandomOptionWithAllergy(drinks);
    }

    // Метод для перевірки, чи містить страву інгредієнти, на які користувач має алергію
    private boolean optionContainsAllergies(MealOption option, List<UserAllergy> allergies) {
        List<Ingredients> ingredients = hibernateMethods.getIngredientsByMealOption(option);
        for (Ingredients ingredient : ingredients) {
            for (UserAllergy allergy : allergies) {
                if (allergy.getIngredients().equals(ingredient)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Метод для отримання випадкової опції зі списку
    private <T> T getRandomOptionWithAllergy(List<T> options) {
        Random random = new Random();
        int index = random.nextInt(options.size());
        return options.get(index);
    }

    // Метод для перевірки, чи містить напій інгредієнти, на які користувач має алергію
    private boolean drinkContainsAllergies(Drink drink, List<UserAllergy> allergies) {
        List<Ingredients> ingredients = hibernateMethods.getIngredientsByDrink(drink);
        for (Ingredients ingredient : ingredients) {
            for (UserAllergy allergy : allergies) {
                if (allergy.getIngredients().equals(ingredient)) {
                    return true;
                }
            }
        }
        return false;
    }

    private MealOption getRandomOption(List<MealOption> options) {
        Random random = new Random();
        int index = random.nextInt(options.size());
        return options.get(index);
    }

    private Drink getRandomDrink() {
        List<Drink> drinks = hibernateMethods.getAllDrinks();
        Random random = new Random();
        int index = random.nextInt(drinks.size());
        return drinks.get(index);
    }

    private User getUserFromApplicationContext() {
        return ApplicationContext.getInstance().getCurrentUser();
    }
    //Метод для перерахунку грам із калорійності продукту на 100 грам та загального калорійного споживання
    private double getGramsFromCalories(double caloriesPer100g, double totalCalories) {
        return totalCalories / (caloriesPer100g / 100);
    }

}