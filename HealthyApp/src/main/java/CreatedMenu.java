import Method.CalorieCalculator;
import entity.*;
import util.HibernateMethods;

import java.util.List;
import java.util.Random;

public class CreatedMenu {
    private HibernateMethods hibernateMethods = new HibernateMethods();
    public void createMenu() {
        // Получаем информацию о пользователе
        User user = getUserFromApplicationContext();
        // Проверяем наличие аллергий у пользователя
        boolean hasAllergy = userHasAllergy(user);

        // Если у пользователя нет аллергии, выбираем случайные блюда для завтрака, обеда и ужина
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
        // Получаем доступные опции для завтрака, обеда и ужина
        List<MealOption> breakfastOptions = hibernateMethods.getAllMealOptionsByType(MealType.Breakfast);
        List<MealOption> lunchOptions = hibernateMethods.getAllMealOptionsByType(MealType.Lunch);
        List<MealOption> dinnerOptions = hibernateMethods.getAllMealOptionsByType(MealType.Dinner);

        // Выбираем случайное блюдо для завтрака, обеда и ужина
        MealOption breakfast = getRandomOption(breakfastOptions);
        MealOption lunch = getRandomOption(lunchOptions);
        MealOption dinner = getRandomOption(dinnerOptions);

        // Получаем случайный напиток для завтрака, обеда и ужина
        Drink breakfastDrink = getRandomDrink();
        Drink lunchDrink = getRandomDrink();
        Drink dinnerDrink = getRandomDrink();
        printSelectedMenuAndCalculateCalories(user, breakfast, lunch, dinner, breakfastDrink, lunchDrink, dinnerDrink);
    }


    private void selectRandomMenuWithUserAllergy(User user) {
        // Получаем доступные опции для завтрака, обеда и ужина
        List<MealOption> breakfastOptions = hibernateMethods.getAllMealOptionsByType(MealType.Breakfast);
        List<MealOption> lunchOptions = hibernateMethods.getAllMealOptionsByType(MealType.Lunch);
        List<MealOption> dinnerOptions = hibernateMethods.getAllMealOptionsByType(MealType.Dinner);
        List<Drink> drinkOptions = hibernateMethods.getAllDrinks();

        // Выбираем случайное блюдо для завтрака, обеда и ужина, учитывая аллергии пользователя
        MealOption breakfast = getRandomOptionWithAllergy(breakfastOptions, user);
        MealOption lunch = getRandomOptionWithAllergy(lunchOptions, user);
        MealOption dinner = getRandomOptionWithAllergy(dinnerOptions, user);

        // Выбираем случайный напиток для завтрака, обеда и ужина, учитывая аллергии пользователя
        Drink breakfastDrink = getRandomDrinkWithAllergy(drinkOptions, user);
        Drink lunchDrink = getRandomDrinkWithAllergy(drinkOptions, user);
        Drink dinnerDrink = getRandomDrinkWithAllergy(drinkOptions, user);
        printSelectedMenuAndCalculateCalories(user, breakfast, lunch, dinner, breakfastDrink, lunchDrink, dinnerDrink);
    }


    private void printSelectedMenuAndCalculateCalories(User user, MealOption breakfast, MealOption lunch, MealOption dinner, Drink breakfastDrink, Drink lunchDrink, Drink dinnerDrink) {
        // Выводим выбранные блюда на экран
        System.out.println("Завтрак: " + breakfast.getName());
        System.out.println("Обед: " + lunch.getName());
        System.out.println("Ужин: " + dinner.getName());

        // Выводим выбранные напитки на экран
        System.out.println("Напиток к завтраку: " + breakfastDrink.getNameDrink());
        System.out.println("Напиток к обеду: " + lunchDrink.getNameDrink());
        System.out.println("Напиток к ужину: " + dinnerDrink.getNameDrink());

        // Рассчитываем общий калораж для каждого приема пищи
        double totalCalories = user.getTotalCaloricUser();
        double breakfastCalories = CalorieCalculator.numberOfGramsForBreakfast(totalCalories);
        double lunchCalories = CalorieCalculator.numberOfGramsForLunch(totalCalories);
        double dinnerCalories = CalorieCalculator.numberOfGramsForDinner(totalCalories);

        // Учитываем калории напитков
        double breakfastDrinkCalories = breakfastDrink != null ? (breakfastDrink.getCaloriesDrink() * 2) : 0;
        double lunchDrinkCalories = lunchDrink != null ? (lunchDrink.getCaloriesDrink() * 2) : 0;
        double dinnerDrinkCalories = dinnerDrink != null ? (dinnerDrink.getCaloriesDrink() * 2) : 0;

        // Вычитаем калории напитков из общего калоража
        breakfastCalories -= breakfastDrinkCalories;
        lunchCalories -= lunchDrinkCalories;
        dinnerCalories -= dinnerDrinkCalories;

        // Получаем количество грамм для каждого блюда
        double breakfastGrams = Math.round((getGramsFromCalories(breakfast.getCaloriesMealOption(), breakfastCalories) * 10.0) / 10.0);
        double lunchGrams = Math.round((getGramsFromCalories(lunch.getCaloriesMealOption(), lunchCalories) * 10.0) / 10.0);
        double dinnerGrams = Math.round((getGramsFromCalories(dinner.getCaloriesMealOption(), dinnerCalories) * 10.0) / 10.0);

        // Выводим количество грамм на экран
        System.out.println("Грамм для завтрака: " + breakfastGrams);
        System.out.println("Грамм для обеда: " + lunchGrams);
        System.out.println("Грамм для ужина: " + dinnerGrams);

        // Находим ID выбранных приемов пищи
        int breakfastId = hibernateMethods.findMealIdByName(breakfast.getName());
        int lunchId = hibernateMethods.findMealIdByName(lunch.getName());
        int dinnerId = hibernateMethods.findMealIdByName(dinner.getName());

        // Находим ID выбранных напитков
        int breakfastDrinkId = hibernateMethods.findDrinkIdByName(breakfastDrink.getNameDrink());
        int lunchDrinkId = hibernateMethods.findDrinkIdByName(lunchDrink.getNameDrink());
        int dinnerDrinkId = hibernateMethods.findDrinkIdByName(dinnerDrink.getNameDrink());

        // Сохраняем выбранный пользователем меню в базе данных
        hibernateMethods.saveUserSelectedMenu(user.getPhoneNumber(), breakfastId,
                lunchId, dinnerId, breakfastDrinkId, lunchDrinkId, dinnerDrinkId,
                breakfastGrams, dinnerGrams, lunchGrams);
    }

    // Метод для выбора случайного блюда из списка, учитывая аллергии пользователя
    private MealOption getRandomOptionWithAllergy(List<MealOption> options, User user) {
        List<UserAllergy> userAllergies = hibernateMethods.getUserAllergies(user);
        options.removeIf(option -> optionContainsAllergies(option, userAllergies));
        return getRandomOptionWithAllergy(options);
    }

    // Метод для выбора случайного напитка из списка, учитывая аллергии пользователя
    private Drink getRandomDrinkWithAllergy(List<Drink> drinks, User user) {
        List<UserAllergy> userAllergies = hibernateMethods.getUserAllergies(user);
        drinks.removeIf(drink -> drinkContainsAllergies(drink, userAllergies));
        return getRandomOptionWithAllergy(drinks);
    }

    // Метод для проверки, содержит ли блюдо ингредиенты, на которые у пользователя есть аллергия
    private boolean optionContainsAllergies(MealOption option, List<UserAllergy> allergies) {
        List<Ingredients> ingredients = hibernateMethods.getIngredientsByMealOption(option);
        for (Ingredients ingredient : ingredients) {
            for (UserAllergy allergy : allergies) {
                if (allergy.getIngredients().equals(ingredient)) {
                    return true; // Если найден ингредиент, на который есть аллергия, возвращаем true
                }
            }
        }
        return false; // Если ни одного аллергена не найдено, возвращаем false
    }

    // Метод для получения случайной опции из списка
    private <T> T getRandomOptionWithAllergy(List<T> options) {
        Random random = new Random();
        int index = random.nextInt(options.size());
        return options.get(index);
    }

    // Метод для проверки, содержит ли напиток ингредиенты, на которые у пользователя есть аллергия
    private boolean drinkContainsAllergies(Drink drink, List<UserAllergy> allergies) {
        List<Ingredients> ingredients = hibernateMethods.getIngredientsByDrink(drink);
        for (Ingredients ingredient : ingredients) {
            for (UserAllergy allergy : allergies) {
                if (allergy.getIngredients().equals(ingredient)) {
                    return true; // Если найден ингредиент, на который есть аллергия, возвращаем true
                }
            }
        }
        return false; // Если ни одного аллергена не найдено, возвращаем false
    }


    private MealOption getRandomOption(List<MealOption> options) {
        // Получаем случайный индекс из списка опций
        Random random = new Random();
        int index = random.nextInt(options.size());
        return options.get(index);
    }

    private Drink getRandomDrink() {
        // Получаем список всех доступных напитков
        List<Drink> drinks = hibernateMethods.getAllDrinks();
        // Выбираем случайный напиток из списка
        Random random = new Random();
        int index = random.nextInt(drinks.size());
        return drinks.get(index);
    }

    private User getUserFromApplicationContext() {
        return ApplicationContext.getInstance().getCurrentUser();
    }
    // Метод для пересчета грамм из калорийности продукта на 100 грамм и общего калорийного потребления
    private double getGramsFromCalories(double caloriesPer100g, double totalCalories) {
        return totalCalories / (caloriesPer100g / 100);
    }

}