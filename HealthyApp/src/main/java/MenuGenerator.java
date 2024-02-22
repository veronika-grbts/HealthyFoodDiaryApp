import Method.CalorieCalculator;
import entity.*;

import java.util.*;

import util.HibernateMethods;

/*public class MenuGenerator {
    private HibernateMethods hibernateMethods = new HibernateMethods();

    public void generateAndSaveMenu(long phoneNumber) {
        User user = getUserFromApplicationContext();

        if (user == null) {
            System.out.println("Пользователь с таким номером телефона не найден.");
            return;
        }

        // Создание новой записи в таблице UserSelectedMenu для данного пользователя
        UserSelectedMenu userSelectedMenu = new UserSelectedMenu();
        userSelectedMenu.setPhoneNumber(user.getPhoneNumber());

        // Получение калорий для каждого приема пищи
        double breakfastCalories = user.getTotalCaloricUser() * CalorieCalculator.numberOfGramsForBreakfast(1550);
        double lunchCalories = user.getTotalCaloricUser() * CalorieCalculator.numberOfGramsForLunch(1550);
        double dinnerCalories = user.getTotalCaloricUser() * CalorieCalculator.numberOfGramsForDinner(1550);

        // Получение доступных завтраков, обедов и ужинов из базы данных
        List<MealOption> breakfastOptions = hibernateMethods.getAllMealOptionsByType(MealType.Breakfast);
        List<MealOption> lunchOptions = hibernateMethods.getAllMealOptionsByType(MealType.Lunch);
        List<MealOption> dinnerOptions = hibernateMethods.getAllMealOptionsByType(MealType.Dinner);

        // Создание списка для хранения выбранных блюд
        List<MealOption> selectedBreakfasts = selectMealOptions(breakfastOptions, breakfastCalories, user);
        List<MealOption> selectedLunches = selectMealOptions(lunchOptions, lunchCalories, user);
        List<MealOption> selectedDinners = selectMealOptions(dinnerOptions, dinnerCalories, user);

// Выбор случайного блюда из доступных в завтраках, обедах и ужинах
        MealOption randomBreakfast = selectRandomMealOption(breakfastOptions, breakfastCalories, user);
        MealOption randomLunch = selectRandomMealOption(lunchOptions, lunchCalories, user);
        MealOption randomDinner = selectRandomMealOption(dinnerOptions, dinnerCalories, user);

// Получение случайного напитка для завтрака, обеда и ужина
        Drink randomBreakfastDrink = selectRandomDrink();
        Drink randomLunchDrink = selectRandomDrink();
        Drink randomDinnerDrink = selectRandomDrink();

// Получение идентификаторов выбранных блюд и напитков по их названию
        Integer breakfastId = hibernateMethods.findMealIdByName(randomBreakfast.getName());
        Integer lunchId = hibernateMethods.findMealIdByName(randomLunch.getName());
        Integer dinnerId = hibernateMethods.findMealIdByName(randomDinner.getName());
        Integer breakfastDrinkId = hibernateMethods.findDrinkIdByName(randomBreakfastDrink.getNameDrink());
        Integer lunchDrinkId = hibernateMethods.findDrinkIdByName(randomLunchDrink.getNameDrink());
        Integer dinnerDrinkId = hibernateMethods.findDrinkIdByName(randomDinnerDrink.getNameDrink());

        // Добавление выбранных блюд в меню пользователя и вывод в консоль
        System.out.println("Завтрак:");
        double breakfastGrams = addMealToMenu(selectedBreakfasts, userSelectedMenu, MealType.Breakfast);
        System.out.println("Обед:");
        double lunchGrams = addMealToMenu(selectedLunches, userSelectedMenu, MealType.Lunch);
        System.out.println("Ужин:");
        double dinnerGrams = addMealToMenu(selectedDinners, userSelectedMenu, MealType.Dinner);

        // Сохранение выбранного меню пользователя в базе данных
        hibernateMethods.saveUserSelectedMenu(user.getPhoneNumber(), );

        // Вывод количества грамм в консоль
        System.out.println("Граммы для завтрака: " + breakfastGrams);
        System.out.println("Граммы для обеда: " + lunchGrams);
        System.out.println("Граммы для ужина: " + dinnerGrams);
    }

    private MealOption selectRandomMealOption(List<MealOption> options, double calories, User user) {
        List<MealOption> validOptions = selectMealOptions(options, calories, user);
        if (validOptions.isEmpty()) {
            return null; // Возвращаем null, если нет доступных вариантов
        }
        Random random = new Random();
        return validOptions.get(random.nextInt(validOptions.size()));
    }

    private Drink selectRandomDrink(List<Drink> drinks, double calories, User user) {
        List<Drink> validOptions = selectMealOptions(drinks, calories, user);
        if (validOptions.isEmpty()) {
            return null; // Возвращаем null, если нет доступных вариантов
        }
        Random random = new Random();
        return validOptions.get(random.nextInt(validOptions.size()));
    }

    private List<MealOption> selectMealOptions(List<MealOption> options, double calories, User user) {
        // Создание списка для хранения выбранных блюд
        List<MealOption> selectedOptions = new ArrayList<>();
        Random random = new Random();

        // Проверяем наличие аллергии у пользователя
        boolean hasAllergy = user.isAllergiesUser();

        // Получаем список ингредиентов, вызывающих аллергическую реакцию
        Set<Ingredients> allergicIngredients = new HashSet<>();
        if (hasAllergy) {
            allergicIngredients = hibernateMethods.getAllergicIngredientsForUser(user);
        }

        // Выбор блюд до достижения необходимого количества калорий
        double totalCalories = user.getTotalCaloricUser();
        while (totalCalories < calories && !options.isEmpty()) {
            // Выбор случайного блюда из списка доступных
            int index = random.nextInt(options.size());
            MealOption option = options.get(index);
            // Проверка наличия аллергии и исключение блюд с аллергическими ингредиентами
            if (!hasAllergy || !containsAllergicIngredients(option, allergicIngredients)) {
                // Добавление блюда в список выбранных и увеличение общего количества калорий
                selectedOptions.add(option);
                totalCalories += option.getCaloriesMealOption();
            }
            // Удаление выбранного блюда из списка доступных, чтобы избежать повторного выбора
            options.remove(index);
        }
        return selectedOptions;
    }

    // Метод для проверки, содержит ли блюдо аллергические ингредиенты
    private boolean containsAllergicIngredients(MealOption option, Set<Ingredients> allergicIngredients) {
        for (Ingredients ingredient : option.getIngredients()) {
            if (allergicIngredients.contains(ingredient)) {
                return true;
            }
        }
        return false;
    }

    private double addMealToMenu(List<MealOption> mealOptions, UserSelectedMenu userSelectedMenu, MealType mealType) {
        double totalGrams = 0;
        for (MealOption mealOption : mealOptions) {
            // Добавление блюда в меню пользователя
            MenuItem menuItem = new MenuItem();
            menuItem.setMealType(mealType);
            menuItem.setMealOption(mealOption);
            menuItem.setUserSelectedMenu(userSelectedMenu);
            userSelectedMenu.getItems().add(menuItem);
            totalGrams += mealOption.getCalories();
        }
        return totalGrams;
    }

    private User getUserFromApplicationContext() {
        return ApplicationContext.getInstance().getCurrentUser();
    }
}*/
