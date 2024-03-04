package project.controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


import project.method.LoseWeightCalculator;
import project.singleton.ApplicationContext;
import project.method.CalorieCalculator;
import project.entity.ActivityLevel;
import project.entity.Ingredients;
import project.entity.User;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;
import project.util.HibernateMethods;


@Slf4j
public class SignUpController {
    private HibernateMethods hibernateMethods = new HibernateMethods();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane yourImageView;

    @FXML
    private AnchorPane imagePlus;

    @FXML
    private AnchorPane signUpPane1;

    @FXML
    private TextField signUpName;

    @FXML
    private TextField sighUpPhone;

    @FXML
    private Button nextBtn1;

    @FXML
    private AnchorPane signUpPane2;

    @FXML
    private TextField signUpAge;

    @FXML
    private TextField signUpWeight;

    @FXML
    private TextField signUpHeight;

    @FXML
    private ComboBox<ActivityLevel> signUpActivityLevel;

    @FXML
    private ComboBox<String> signUpGender;

    @FXML
    private Button nextBtn2;

    @FXML
    private AnchorPane signUpPane3;

    @FXML
    private CheckBox checkBoxAllergy;

    @FXML
    private CheckBox checkBoxCause;

    @FXML
    private Button finishBtn;

    @FXML
    private ImageView ImagePlus;

    @FXML
    void Handle(MouseEvent event) {
        double x = 14; // координа Х для ComboBox
        double y = 100; // координа У для ComboBox
        double deltaY = 40; // отсутп
        ComboBox<String> newComboBox = new ComboBox<>();

        // Определяем координаты для нового ComboBox
        int numberOfExistingComboBoxes = signUpPane3.getChildren().stream()
                .filter(node -> node instanceof ComboBox)
                .mapToInt(node -> 1)
                .sum();

        y += numberOfExistingComboBoxes * deltaY; // Добавляем отступ в зависимости от количества существующих ComboBox
        newComboBox.setLayoutX(x);
        newComboBox.setLayoutY(y);
        newComboBox.setPrefWidth(300);
        newComboBox.setPrefHeight(30);

        newComboBox.setEditable(true);
        List<Ingredients> allIngredients = hibernateMethods.getAllIngredients();
        if (allIngredients != null) {
            for (Ingredients ingredient : allIngredients) {
                newComboBox.getItems().add(ingredient.getNameIngredients());
            }

            signUpPane3.getChildren().add(newComboBox);

            // Додаємо обработчика события для выбора элемента в новом ComboBox
            newComboBox.setOnAction(e -> {
                String selectedProduct = newComboBox.getValue();
                Ingredients ingredient = hibernateMethods.findIngredientByName(selectedProduct);
                if (ingredient != null) {
                    hibernateMethods.addUserAllergyForUser(
                            Long.parseLong(sighUpPhone.getText()),
                            ingredient.getIdIngredients());
                } else {
                    log.warn("the selected ingredient  was not found");
                }
            });

            // Добавляем слушатель событий на текстовое поле ComboBox для фильтрации
            newComboBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
                filterComboBox(newComboBox, newValue);
            });
        }
    }

    @FXML
    void initialize() {
        signUpActivityLevel.getItems().addAll(ActivityLevel.High,
                ActivityLevel.Medium, ActivityLevel.Low);

        signUpGender.getItems().addAll("чоловік", "жінка");

        nextBtn1.setOnAction(event -> {
            signUpPane1.setVisible(false);
            signUpPane2.setVisible(true);
        });

        nextBtn2.setOnAction(event -> {
            signUpPane2.setVisible(false);
            signUpPane3.setVisible(true);
            imagePlus.setOnMouseClicked(this::Handle);
        });

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
            hibernateMethods.createUser(Long.parseLong(phone), name, age, weight,
                    height, gender, activityLevel, hasAllergy, hasCause,
                    calories, protein, fat, carbs);

            if (hasAllergy) {
                for (Node node : signUpPane3.getChildren()) {
                    if (node instanceof ComboBox) {
                        ComboBox<String> comboBox = (ComboBox<String>) node;
                        String selectedProduct = comboBox.getValue();
                        if (selectedProduct != null && !selectedProduct.isEmpty()) {
                            Ingredients ingredient = hibernateMethods.findIngredientByName(selectedProduct);
                            if (ingredient != null) {
                                hibernateMethods.addUserAllergyForUser(Long.parseLong(phone),
                                        ingredient.getIdIngredients());

                            } else {
                                log.warn("the selected ingredient  was not found");
                            }
                        }
                    }
                }
            }

            User newUser = hibernateMethods.getUserInfo(Long.parseLong(phone));
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

                    hibernateMethods.saveWeightLossGoal(
                            newUser, newUser.getWeightUser(), targetWeight,
                            targetCaloricDeficit, estimatedCompletionTime, caloriesWithLosingWeight
                    );

                    hibernateMethods.updateUserDataByPhoneNumber(
                            newUser.getPhoneNumber(), caloriesWithLosingWeight,
                            proteinWithLosingWeight, fatWithLosingWeight, carbsWithLosingWeight
                    );

                    ApplicationContext.getInstance().setCurrentUser(
                            hibernateMethods.getUserByPhoneNumber(newUser.getPhoneNumber())
                    ); }else {
                    hibernateMethods.updateUserCause(newUser.getPhoneNumber(), false);
                    ApplicationContext.getInstance()
                            .setCurrentUser(hibernateMethods.getUserByPhoneNumber(newUser.getPhoneNumber()));
                }
            }
            try {
                HibbernateRunner.setRoot("mainpage");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    private void filterComboBox(ComboBox<String> comboBox, String filter) {
        if (filter == null || filter.isEmpty()) {
            comboBox.getItems().clear();
            List<Ingredients> allIngredients = hibernateMethods.getAllIngredients();
            if (allIngredients != null) {
                for (Ingredients ingredient : allIngredients) {
                    comboBox.getItems().add(ingredient.getNameIngredients());
                }
            }
            comboBox.hide();
        } else {
            Predicate<String> predicate = item -> item.toLowerCase().contains(filter.toLowerCase());
            List<String> filteredItems = comboBox.getItems().stream()
                    .filter(predicate).collect(Collectors.toList());
            comboBox.getItems().setAll(filteredItems);
            comboBox.show();
        }
    }
}
