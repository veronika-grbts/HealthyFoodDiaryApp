package project.method;
import project.singleton.ApplicationContext;
import project.entity.*;
import project.util.HibernateMethods;

import java.util.List;
import java.util.Random;


public class CreatedMenu {
    private HibernateMethods hibernateMethods = new HibernateMethods();
    private CalorieCalculator calorieCalculator = new CalorieCalculator();

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
        List<MealOption> snackOptions = hibernateMethods.getAllMealOptionsByType(MealType.Snack);

        MealOption breakfast = getRandomOption(breakfastOptions);
        MealOption lunch = getRandomOption(lunchOptions);
        MealOption dinner = getRandomOptionSecond(dinnerOptions);
        MealOption snack = getRandomOptionSecond(snackOptions);
        MealOption snackSecond = getRandomOptionSecond(snackOptions);

        Drink breakfastDrink = getRandomDrink();
        Drink lunchDrink = getRandomDrink();
        Drink dinnerDrink = getRandomDrink();

        // Проверяем, есть ли для выбранного основного блюда дополнительное блюдо
        if (lunch.getAdditionalDishId() != null && dinner.getAdditionalDishId() != null) {
            // Если есть, получаем информацию о дополнительном блюде
            MealOption additionalDish = hibernateMethods.getMealOptionById(lunch.getAdditionalDishId());
            MealOption dinnerAdditionalDish = hibernateMethods.getMealOptionById(dinner.getAdditionalDishId());
            // Добавляем основное и дополнительное блюда в меню
            printSelectedMenuAndCalculateCalories(user, breakfast,lunch, dinner, additionalDish,dinnerAdditionalDish, snack,snackSecond, breakfastDrink, lunchDrink, dinnerDrink);
        }
        else if (lunch.getAdditionalDishId() == null && dinner.getAdditionalDishId() != null) {
            MealOption dinnerAdditionalDish = hibernateMethods.getMealOptionById(dinner.getAdditionalDishId());
            printSelectedMenuAndCalculateCalories(user, breakfast,lunch, dinner, null,dinnerAdditionalDish, snack,snackSecond, breakfastDrink, lunchDrink, dinnerDrink);
        }
        else if (lunch.getAdditionalDishId() != null && dinner.getAdditionalDishId() == null) {
            MealOption additionalDish = hibernateMethods.getMealOptionById(lunch.getAdditionalDishId());
            printSelectedMenuAndCalculateCalories(user, breakfast,lunch, dinner, additionalDish,null, snack,snackSecond, breakfastDrink, lunchDrink, dinnerDrink);
        }
        else {
            printSelectedMenuAndCalculateCalories(user, breakfast,lunch, dinner, null, null, snack,snackSecond, breakfastDrink, lunchDrink, dinnerDrink);
        }
    }

    private void selectRandomMenuWithUserAllergy(User user) {
        List<MealOption> breakfastOptions = hibernateMethods.getAllMealOptionsByType(MealType.Breakfast);
        List<MealOption> lunchOptions = hibernateMethods.getAllMealOptionsByType(MealType.Lunch);
        List<MealOption> dinnerOptions = hibernateMethods.getAllMealOptionsByType(MealType.Dinner);
        List<MealOption> snackOptions = hibernateMethods.getAllMealOptionsByType(MealType.Snack);

        List<Drink> drinkOptions = hibernateMethods.getAllDrinks();

        MealOption breakfast = getRandomOptionWithAllergy(breakfastOptions, user);
        MealOption lunch = getRandomOptionWithAllergy(lunchOptions, user);
        MealOption dinner = getRandomOptionWithAllergy(dinnerOptions, user);
        MealOption snack = getRandomOptionWithAllergy(snackOptions, user);
        MealOption snackSecond = getRandomOptionWithAllergy(snackOptions, user);

        Drink breakfastDrink = getRandomDrinkWithAllergy(drinkOptions, user);
        Drink lunchDrink = getRandomDrinkWithAllergy(drinkOptions, user);
        Drink dinnerDrink = getRandomDrinkWithAllergy(drinkOptions, user);

        // Проверяем, есть ли для выбранного основного блюда дополнительное блюдо
        if (lunch.getAdditionalDishId() != null && dinner.getAdditionalDishId() != null) {
            // Если есть, получаем информацию о дополнительном блюде
            MealOption additionalDish = hibernateMethods.getMealOptionById(lunch.getAdditionalDishId());
            MealOption dinnerAdditionalDish = hibernateMethods.getMealOptionById(dinner.getAdditionalDishId());
            // Добавляем основное и дополнительное блюда в меню
            printSelectedMenuAndCalculateCalories(user, breakfast,lunch, dinner, additionalDish,dinnerAdditionalDish, snack,snackSecond, breakfastDrink, lunchDrink, dinnerDrink);
        }
        else if (lunch.getAdditionalDishId() == null && dinner.getAdditionalDishId() != null) {
            MealOption dinnerAdditionalDish = hibernateMethods.getMealOptionById(dinner.getAdditionalDishId());
            printSelectedMenuAndCalculateCalories(user, breakfast,lunch, dinner, null,dinnerAdditionalDish, snack,snackSecond, breakfastDrink, lunchDrink, dinnerDrink);
        }
        else if (lunch.getAdditionalDishId() != null && dinner.getAdditionalDishId() == null) {
            MealOption additionalDish = hibernateMethods.getMealOptionById(lunch.getAdditionalDishId());
            printSelectedMenuAndCalculateCalories(user, breakfast,lunch, dinner, additionalDish,null, snack,snackSecond, breakfastDrink, lunchDrink, dinnerDrink);
        }
        else {
            printSelectedMenuAndCalculateCalories(user, breakfast,lunch, dinner, null, null, snack,snackSecond, breakfastDrink, lunchDrink, dinnerDrink);
        }
    }


    private void printSelectedMenuAndCalculateCalories(User user, MealOption breakfast, MealOption lunch, MealOption dinner, MealOption additionalDish, MealOption dinnerAdditionalDish, MealOption snack, MealOption snackSecond, Drink breakfastDrink, Drink lunchDrink, Drink dinnerDrink) {
        // Расчет общего количества калорий для каждого приема пищи
        double totalCalories = user.getTotalCaloricUser();
        double breakfastCalories = CalorieCalculator.numberOfGramsForBreakfast(totalCalories);
        double lunchCalories = CalorieCalculator.numberOfGramsForLunch(totalCalories);
        double dinnerCalories = CalorieCalculator.numberOfGramsForDinner(totalCalories);
        double snackCalories = CalorieCalculator.numberOfGramsForSnack(totalCalories);
        double snackSecondCalories = CalorieCalculator.numberOfGramsForSnack(totalCalories);

        // Расчет калорий от напитков для каждого приема пищи
        double breakfastDrinkCalories = breakfastDrink != null ? (breakfastDrink.getCaloriesDrink() * 2) : 0;
        double lunchDrinkCalories = lunchDrink != null ? (lunchDrink.getCaloriesDrink() * 2) : 0;
        double dinnerDrinkCalories = dinnerDrink != null ? (dinnerDrink.getCaloriesDrink() * 2) : 0;

        // Вычитание калорий от напитков из общего количества калорий для каждого приема пищи
        breakfastCalories -= breakfastDrinkCalories;
        lunchCalories -= lunchDrinkCalories;
        dinnerCalories -= dinnerDrinkCalories;

        // Получение количества грамм для каждого блюда
        double breakfastGrams = Math.round((getGramsFromCalories(breakfast.getCaloriesMealOption(), breakfastCalories) * 10.0) / 10.0);
        double lunchGrams = Math.round((getGramsFromCalories(lunch.getCaloriesMealOption(), lunchCalories) * 10.0) / 10.0);
        double dinnerGrams = Math.round((getGramsFromCalories(dinner.getCaloriesMealOption(), dinnerCalories) * 10.0) / 10.0);
        double snackFirstGrams = Math.round((getGramsFromCalories(snack.getCaloriesMealOption(), snackCalories) * 10.0) / 10.0);
        double snackSecondGrams = Math.round((getGramsFromCalories(snackSecond.getCaloriesMealOption(), snackSecondCalories) * 10.0) / 10.0);

        int breakfastId = 0;
        int lunchId = 0;
        int dinnerId = 0;
        int snackId = 0;
        int snackSecondId = 0;

        // Обработка дополнительного блюда для обеда
        Double additionalDishGrams = null;
        Double dinnerAdditionalDishGrams = null;

        if (additionalDish != null && lunch != null) {
            additionalDishGrams = lunchGrams * 0.6;
            lunchGrams -= additionalDishGrams; // Вычитание количества граммов для дополнительного блюда из общего количества калорий обеда
        }

        if (dinnerAdditionalDish != null && dinner != null) {
            dinnerAdditionalDishGrams = dinnerGrams * 0.6;
            dinnerGrams -= dinnerAdditionalDishGrams; // Вычитание количества граммов для дополнительного блюда из общего количества калорий ужина
        }

        if (breakfast != null) {
            breakfastId = hibernateMethods.findMealIdByName(breakfast.getName());
            // Остальной код
        } else {
            System.out.println("Завтрака не будет " + breakfast);
            throw new RuntimeException("Завтрака не будет " + breakfast);
        }
        if (snack != null) {
            snackId = hibernateMethods.findMealIdByName(snack.getName());
            // Остальной код
        } else {
            System.out.println("Перекус не будет " + snack);
            throw new RuntimeException("Перекуса не будет " + snack);
        }
        if (snackSecond != null) {
            snackSecondId = hibernateMethods.findMealIdByName(snackSecond.getName());
            // Остальной код
        } else {
            System.out.println("Перекус два не будет " + snackSecond);
            throw new RuntimeException("Перекуса 2 не будет " + snackSecond);
        }
        if (lunch != null) {
            lunchId = hibernateMethods.findMealIdByName(lunch.getName());
            // Остальной код
        }else {
            System.out.println("Обеда не будет " + lunch);
            throw new RuntimeException("Обеда не будет " + lunch);
        }
        Integer additionalDishId = null;
        if (additionalDish != null && lunch != null) {
            additionalDishId = hibernateMethods.findAdditionalDishIdByMainDish(lunch);
        }

        if (dinner != null) {
            dinnerId = hibernateMethods.findMealIdByName(dinner.getName());
            // Остальной код
        }else {
            System.out.println("Ужина не будет не будет " + dinner);
            throw new RuntimeException("ужина не будет " + dinner);
        }
        Integer dinnerAdditionalDishId = null;
        if (dinnerAdditionalDish != null && dinner != null) {
            dinnerAdditionalDishId = hibernateMethods.findAdditionalDishIdByMainDish(dinner);
        }

        int breakfastDrinkId = breakfastDrink != null ? hibernateMethods.findDrinkIdByName(breakfastDrink.getNameDrink()) : null;
        int lunchDrinkId = lunchDrink != null ? hibernateMethods.findDrinkIdByName(lunchDrink.getNameDrink()) : null;
        int dinnerDrinkId = dinnerDrink != null ? hibernateMethods.findDrinkIdByName(dinnerDrink.getNameDrink()) : null;

        // Получаем питательную ценность для каждого блюда
        Nutrition breakfastNutrition = new Nutrition(breakfast.getProteinMealOption(), breakfast.getFatMealOption(), breakfast.getCarbsMealOption());
        Nutrition snackFirstNutrition = new Nutrition(snack.getProteinMealOption(), snack.getFatMealOption(), snack.getCarbsMealOption());
        Nutrition snackSecondNutrition = new Nutrition(snackSecond.getProteinMealOption(), snackSecond.getFatMealOption(), snackSecond.getCarbsMealOption());
        Nutrition lunchNutrition = new Nutrition(lunch.getProteinMealOption(), lunch.getFatMealOption(), lunch.getCarbsMealOption());
        Nutrition additionalDishNutrition = null;

        if (additionalDish != null) {
            additionalDishNutrition = new Nutrition(additionalDish.getProteinMealOption(), additionalDish.getFatMealOption(), additionalDish.getCarbsMealOption());
        }

        Nutrition dinnerNutrition = new Nutrition(dinner.getProteinMealOption(), dinner.getFatMealOption(), dinner.getCarbsMealOption());
        Nutrition dinnerAdditionalDishNutrition = null;
        if (dinnerAdditionalDish != null) {
            dinnerAdditionalDishNutrition = new Nutrition(dinnerAdditionalDish.getProteinMealOption(), dinnerAdditionalDish.getFatMealOption(), dinnerAdditionalDish.getCarbsMealOption());
        }

        // Расчет питательной ценности от напитков для каждого приема пищи с учетом количества грамм
        double breakfastDrinkProtein = breakfastDrink != null ? (breakfastDrink.getProteinDrink() * 2) : 0;
        double breakfastDrinkFat = breakfastDrink != null ? (breakfastDrink.getFatDrink() * 2) : 0;
        double breakfastDrinkCarbs = breakfastDrink != null ? (breakfastDrink.getCarbsDrink() * 2) : 0;

        double lunchDrinkProtein = lunchDrink != null ? (lunchDrink.getProteinDrink() * 2) : 0;
        double lunchDrinkFat = lunchDrink != null ? (lunchDrink.getFatDrink() * 2) : 0;
        double lunchDrinkCarbs = lunchDrink != null ? (lunchDrink.getCarbsDrink() * 2) : 0;

        double dinnerDrinkProtein = dinnerDrink != null ? (dinnerDrink.getProteinDrink() * 2) : 0;
        double dinnerDrinkFat = dinnerDrink != null ? (dinnerDrink.getFatDrink() * 2) : 0;
        double dinnerDrinkCarbs = dinnerDrink != null ? (dinnerDrink.getCarbsDrink() * 2) : 0;

        Nutrition totalNutrition = null;
        // Расчет общей питательной ценности для каждого приема пищи с учетом количества грамм
        if (additionalDish != null && dinnerAdditionalDish != null) {
            totalNutrition = new Nutrition(
                    (breakfastNutrition.getProtein() * breakfastGrams / 100) + (snackFirstNutrition.getProtein() * snackFirstGrams / 100)+ (snackSecondNutrition.getProtein() * snackSecondGrams / 100)+ (lunchNutrition.getProtein() * lunchGrams / 100) + (additionalDishNutrition.getProtein() * additionalDishGrams / 100)+ (dinnerAdditionalDishNutrition.getProtein() * dinnerAdditionalDishGrams / 100) + (dinnerNutrition.getProtein() * dinnerGrams / 100) + breakfastDrinkProtein + lunchDrinkProtein + dinnerDrinkProtein,
                    (breakfastNutrition.getFat() * breakfastGrams / 100) + (snackFirstNutrition.getFat() * snackFirstGrams / 100)+ (snackSecondNutrition.getFat() * snackSecondGrams / 100) + (lunchNutrition.getFat() * lunchGrams / 100)  + (additionalDishNutrition.getFat() * additionalDishGrams / 100)+ (dinnerAdditionalDishNutrition.getFat() * dinnerAdditionalDishGrams / 100) + (dinnerNutrition.getFat() * dinnerGrams / 100)+ breakfastDrinkFat + lunchDrinkFat + dinnerDrinkFat,
                    (breakfastNutrition.getCarbohydrates() * breakfastGrams / 100) + (snackFirstNutrition.getCarbohydrates() * snackFirstGrams / 100)  + (snackSecondNutrition.getCarbohydrates() * snackSecondGrams / 100) +(lunchNutrition.getCarbohydrates() * lunchGrams / 100)+ (additionalDishNutrition.getCarbohydrates() * additionalDishGrams / 100) + (dinnerAdditionalDishNutrition.getCarbohydrates() * dinnerAdditionalDishGrams / 100) + (dinnerNutrition.getCarbohydrates() * dinnerGrams / 100) + breakfastDrinkCarbs + lunchDrinkCarbs + dinnerDrinkCarbs
            );
        }
        else if (additionalDish == null && dinnerAdditionalDish != null)  {
            totalNutrition = new Nutrition(
                    (breakfastNutrition.getProtein() * breakfastGrams / 100) + (snackFirstNutrition.getProtein() * snackFirstGrams / 100)+ (snackSecondNutrition.getProtein() * snackSecondGrams / 100)+ (lunchNutrition.getProtein() * lunchGrams / 100) + (dinnerAdditionalDishNutrition.getProtein() * dinnerAdditionalDishGrams / 100) + (dinnerNutrition.getProtein() * dinnerGrams / 100) + breakfastDrinkProtein + lunchDrinkProtein + dinnerDrinkProtein,
                    (breakfastNutrition.getFat() * breakfastGrams / 100) + (snackFirstNutrition.getFat() * snackFirstGrams / 100)+ (snackSecondNutrition.getFat() * snackSecondGrams / 100) + (lunchNutrition.getFat() * lunchGrams / 100) + (dinnerAdditionalDishNutrition.getFat() * dinnerAdditionalDishGrams / 100) + (dinnerNutrition.getFat() * dinnerGrams / 100)+ breakfastDrinkFat + lunchDrinkFat + dinnerDrinkFat,
                    (breakfastNutrition.getCarbohydrates() * breakfastGrams / 100) + (snackFirstNutrition.getCarbohydrates() * snackFirstGrams / 100)  + (snackSecondNutrition.getCarbohydrates() * snackSecondGrams / 100) +(lunchNutrition.getCarbohydrates() * lunchGrams / 100)+ (dinnerAdditionalDishNutrition.getCarbohydrates() * dinnerAdditionalDishGrams / 100) + (dinnerNutrition.getCarbohydrates() * dinnerGrams / 100) + breakfastDrinkCarbs + lunchDrinkCarbs + dinnerDrinkCarbs
            );
        }
        else if (additionalDish != null && dinnerAdditionalDish == null) {
            totalNutrition = new Nutrition(
            (breakfastNutrition.getProtein() * breakfastGrams / 100) + (snackFirstNutrition.getProtein() * snackFirstGrams / 100)+ (snackSecondNutrition.getProtein() * snackSecondGrams / 100)+ (lunchNutrition.getProtein() * lunchGrams / 100) + (additionalDishNutrition.getProtein() * additionalDishGrams / 100) + (dinnerNutrition.getProtein() * dinnerGrams / 100) + breakfastDrinkProtein + lunchDrinkProtein + dinnerDrinkProtein,
                    (breakfastNutrition.getFat() * breakfastGrams / 100) + (snackFirstNutrition.getFat() * snackFirstGrams / 100)+ (snackSecondNutrition.getFat() * snackSecondGrams / 100) + (lunchNutrition.getFat() * lunchGrams / 100)  + (additionalDishNutrition.getFat() * additionalDishGrams / 100) + (dinnerNutrition.getFat() * dinnerGrams / 100)+ breakfastDrinkFat + lunchDrinkFat + dinnerDrinkFat,
                    (breakfastNutrition.getCarbohydrates() * breakfastGrams / 100) + (snackFirstNutrition.getCarbohydrates() * snackFirstGrams / 100)  + (snackSecondNutrition.getCarbohydrates() * snackSecondGrams / 100) +(lunchNutrition.getCarbohydrates() * lunchGrams / 100) + (additionalDishNutrition.getCarbohydrates() * additionalDishGrams / 100) +(dinnerNutrition.getCarbohydrates() * dinnerGrams / 100) + breakfastDrinkCarbs + lunchDrinkCarbs + dinnerDrinkCarbs
            );
        }
        else {
            totalNutrition = new Nutrition(
                    (breakfastNutrition.getProtein() * breakfastGrams / 100) + (snackFirstNutrition.getProtein() * snackFirstGrams / 100)+ (snackSecondNutrition.getProtein() * snackSecondGrams / 100)+ (lunchNutrition.getProtein() * lunchGrams / 100)  + (dinnerNutrition.getProtein() * dinnerGrams / 100) + breakfastDrinkProtein + lunchDrinkProtein + dinnerDrinkProtein,
                    (breakfastNutrition.getFat() * breakfastGrams / 100) + (snackFirstNutrition.getFat() * snackFirstGrams / 100)+ (snackSecondNutrition.getFat() * snackSecondGrams / 100) + (lunchNutrition.getFat() * lunchGrams / 100)  + (dinnerNutrition.getFat() * dinnerGrams / 100)+ breakfastDrinkFat + lunchDrinkFat + dinnerDrinkFat,
                    (breakfastNutrition.getCarbohydrates() * breakfastGrams / 100) + (snackFirstNutrition.getCarbohydrates() * snackFirstGrams / 100)  + (snackSecondNutrition.getCarbohydrates() * snackSecondGrams / 100) +(lunchNutrition.getCarbohydrates() * lunchGrams / 100) + (dinnerNutrition.getCarbohydrates() * dinnerGrams / 100) + breakfastDrinkCarbs + lunchDrinkCarbs + dinnerDrinkCarbs
            );
        }
        // Проверка соответствия общей питательной ценности нормам пользователя с учетом допустимого отклонения в 15%
      if (isNutritionWithinRange(totalNutrition, user)) {
            // Сохранение выбранного меню
            hibernateMethods.saveUserSelectedMenu(user.getPhoneNumber(), breakfastId, snackId, snackSecondId,
                    lunchId, dinnerId, additionalDishId, dinnerAdditionalDishId, breakfastDrinkId,
                    lunchDrinkId, dinnerDrinkId, breakfastGrams, dinnerGrams,
                    lunchGrams, additionalDishGrams,dinnerAdditionalDishGrams, snackFirstGrams, snackSecondGrams);
       } else {
            MealOption newBreakfast = generateRandomMeal(MealType.Breakfast);
          printSelectedMenuAndCalculateCalories(user, newBreakfast, lunch, dinner, additionalDish, dinnerAdditionalDish, snack, snackSecond, breakfastDrink, lunchDrink, dinnerDrink);
        }
    }
    private MealOption generateRandomMeal(MealType mealType) {
        List<MealOption> options = hibernateMethods.getAllMealOptionsByType(mealType);
        return getRandomOption(options);
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
        System.out.println("ID "+index);
        return options.get(index);
    }

    private MealOption getRandomOptionSecond(List<MealOption> options) {
        Random random = new Random();
        // Если список содержит только один элемент, вернуть его
        if (options.size() == 1) {
            return options.get(0);
        } else {
            int index;
            do {
                index = random.nextInt(options.size());
            } while (index == 0); // Генерировать новый индекс, пока он не будет отличен от 0
            System.out.println("ID " + index);
            return options.get(index);
        }
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

    // Метод для проверки соответствия питательной ценности заданным нормам с учетом допустимого отклонения
    private boolean isNutritionWithinRange(Nutrition nutrition, User user) {
        user = getUserFromApplicationContext();
        double totalProteinInMenu = nutrition.getProtein();
        double totalFatInMenu = nutrition.getFat();
        double totalCarbInMenu = nutrition.getCarbohydrates();
        double percent = 0.8;

        double proteinRequirement = user.getTotalProteinUser() * percent;
        double fatRequirement = user.getTotalFatUser() * percent;
        double carbRequirement = user.getTotalCarbsUser() * percent;


        boolean proteinMet = totalProteinInMenu >= proteinRequirement;
        boolean fatMet = totalFatInMenu >= fatRequirement;
        boolean carbMet = totalCarbInMenu >= carbRequirement;
        return proteinMet && fatMet && carbMet;
    }
}