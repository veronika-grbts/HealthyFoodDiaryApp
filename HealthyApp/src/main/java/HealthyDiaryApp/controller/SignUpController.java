/*
 * SignUpController  class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Контролер, що відповідає за обробку подій під час реєстрації користувача.
 */
package HealthyDiaryApp.controller;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import HealthyDiaryApp.HibbernateRunner;
import HealthyDiaryApp.enums.ActivityLevel;
import HealthyDiaryApp.entity.Ingredients;
import HealthyDiaryApp.entity.User;
import HealthyDiaryApp.calculator.CalorieCalculator;
import HealthyDiaryApp.calculator.LoseWeightCalculator;
import HealthyDiaryApp.singleton.ApplicationContext;
import HealthyDiaryApp.util.IngredientComponent;
import HealthyDiaryApp.util.UserComponent;
import HealthyDiaryApp.util.WeightLossGoalsComponent;

import java.io.IOException;

@Slf4j
public class SignUpController {
    private UserComponent userComponent = new UserComponent();
    private IngredientComponent ingredientComponent = new IngredientComponent();
    private WeightLossGoalsComponent weightLossGoalsComponent = new WeightLossGoalsComponent();

    public void handleFinishBtn(Stage stage, Button finishBtn, TextField signUpName, TextField sighUpPhone, TextField signUpAge,
                                TextField signUpWeight, TextField signUpHeight, ComboBox<String> signUpGender,
                                ComboBox<ActivityLevel> signUpActivityLevel, CheckBox checkBoxAllergy,
                                CheckBox checkBoxCause, AnchorPane signUpPane3 ){
        finishBtn.setOnAction(event -> {
            String phone = sighUpPhone.getText();
            String name = signUpName.getText();
            int age = Integer.parseInt(signUpAge.getText());
            double weight = Double.parseDouble(signUpWeight.getText());
            double height = Double.parseDouble(signUpHeight.getText());
            boolean gender = signUpGender.getValue().equals("чоловік");

            ActivityLevel activityLevel = ActivityLevel.valueOf(String.valueOf(signUpActivityLevel.getValue()));

            boolean hasAllergy = checkBoxAllergy.isSelected();
            boolean hasCause = checkBoxCause.isSelected();

            double calories = CalorieCalculator.calculateCalories(weight, height,
                    age, gender, activityLevel);
            double protein = CalorieCalculator.calculateProtein(calories);
            double fat = CalorieCalculator.calculateFat(calories);
            double carbs = CalorieCalculator.calculateCarbs(calories);
            userComponent.createUser(Long.parseLong(phone), name, age, weight,
                    height, gender, activityLevel, hasAllergy, hasCause,
                    calories, protein, fat, carbs);

            if (hasAllergy) {
                for (javafx.scene.Node node : signUpPane3.getChildren()) {
                    if (node instanceof ComboBox) {
                        ComboBox<String> comboBox = (ComboBox<String>) node;
                        String selectedProduct = comboBox.getValue();
                        if (selectedProduct != null && !selectedProduct.isEmpty()) {
                            Ingredients ingredient = ingredientComponent.findIngredientByName(selectedProduct);
                            if (ingredient != null) {
                                userComponent.addUserAllergyForUser(Long.parseLong(phone),
                                        ingredient.getIdIngredients());

                            } else {
                                log.warn("the selected ingredient  was not found");
                            }
                        }
                    }
                }
            }

            User newUser = userComponent.getUserInfo(Long.parseLong(phone));
            ApplicationContext.getInstance().setCurrentUser(newUser);
            if (newUser.isCauseUser()){
                double targetWeight = LoseWeightCalculator.calculateBestWeight(
                        newUser.getHeightUser(),
                        newUser.isGenderUser()
                );
                double targetCaloricDeficit = LoseWeightCalculator.calculatorDeficitCaloric(
                        newUser.getWeightUser(),
                        newUser.getHeightUser(),
                        newUser.getActivityLevel(),
                        newUser.getAgeUser(),
                        newUser.isGenderUser()
                );
                short estimatedCompletionTime = LoseWeightCalculator.estimateTimeToReachGoal(
                        newUser.getWeightUser(),
                        targetWeight,
                        targetCaloricDeficit
                );
                double caloriesWithLosingWeight = LoseWeightCalculator.caloriesDayWithDeficit(
                        newUser.getWeightUser(),
                        newUser.getHeightUser(),
                        newUser.getActivityLevel(),
                        newUser.getAgeUser(),
                        newUser.isGenderUser()
                );

                if((targetWeight < weight) && (18.5 < LoseWeightCalculator.calculateBMI(weight, height))){
                    //изменения калорий, и белков и жиров и углеводов;
                    double proteinWithLosingWeight = CalorieCalculator.calculateProtein(caloriesWithLosingWeight);
                    double fatWithLosingWeight = CalorieCalculator.calculateFat(caloriesWithLosingWeight);
                    double carbsWithLosingWeight = CalorieCalculator.calculateCarbs(caloriesWithLosingWeight);

                    weightLossGoalsComponent.saveWeightLossGoal(
                            newUser, newUser.getWeightUser(), targetWeight,
                            targetCaloricDeficit, estimatedCompletionTime, caloriesWithLosingWeight
                    );

                    userComponent.updateUserDataByPhoneNumber(
                            newUser.getPhoneNumber(), newUser.getWeightUser(), caloriesWithLosingWeight,
                            proteinWithLosingWeight, fatWithLosingWeight, carbsWithLosingWeight
                    );

                    ApplicationContext.getInstance().setCurrentUser(
                            userComponent.getUserByPhoneNumber(newUser.getPhoneNumber())
                    ); }else {
                    userComponent.updateUserCause(newUser.getPhoneNumber(), false);
                    ApplicationContext.getInstance()
                            .setCurrentUser(userComponent.getUserByPhoneNumber(newUser.getPhoneNumber()));
                }
            }
            try {
                HibbernateRunner.setRoot("mainpage");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void handleImagePlusClicked(TextField sighUpPhone, AnchorPane signUpPane3) {
        double x = 14; // координа Х для ComboBox
        double y = 100; // координа У для ComboBox
        double deltaY = 40; // отстутп
        int prefWidth = 300; // newComboBox.setPrefWidth
        int prefHeight = 30; // newComboBox.setPrefHeight
        ComboBox<String> newComboBox = new ComboBox<>();

        int numberOfExistingComboBoxes = signUpPane3.getChildren().stream()
                .filter(node -> node instanceof ComboBox)
                .mapToInt(node -> 1)
                .sum();

        y += numberOfExistingComboBoxes * deltaY;
        newComboBox.setLayoutX(x);
        newComboBox.setLayoutY(y);
        newComboBox.setPrefWidth(prefWidth);
        newComboBox.setPrefHeight(prefHeight);

        newComboBox.setEditable(true);
        java.util.List<Ingredients> allIngredients = ingredientComponent.getAllIngredients();
        if (allIngredients != null) {
            for (Ingredients ingredient : allIngredients) {
                newComboBox.getItems().add(ingredient.getNameIngredients());
            }

            signUpPane3.getChildren().add(newComboBox);


            newComboBox.setOnAction(e -> {
                String selectedProduct = newComboBox.getValue();
                Ingredients ingredient = ingredientComponent.findIngredientByName(selectedProduct);
                if (ingredient != null) {
                    userComponent.addUserAllergyForUser(
                            Long.parseLong(sighUpPhone.getText()),
                            ingredient.getIdIngredients());
                } else {
                    log.warn("the selected ingredient  was not found");
                }
            });


            newComboBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
                filterComboBox(newComboBox, newValue);
            });
        }
    }


    private void filterComboBox(ComboBox<String> comboBox, String filter) {
        if (filter == null || filter.isEmpty()) {
            comboBox.getItems().clear();
            java.util.List<Ingredients> allIngredients = ingredientComponent.getAllIngredients();
            if (allIngredients != null) {
                for (Ingredients ingredient : allIngredients) {
                    comboBox.getItems().add(ingredient.getNameIngredients());
                }
            }
            comboBox.hide();
        } else {
            java.util.function.Predicate<String> predicate = item -> item.toLowerCase().contains(filter.toLowerCase());
            java.util.List<String> filteredItems = comboBox.getItems().stream()
                    .filter(predicate).collect(java.util.stream.Collectors.toList());
            comboBox.getItems().setAll(filteredItems);
            comboBox.show();
        }
    }
}
