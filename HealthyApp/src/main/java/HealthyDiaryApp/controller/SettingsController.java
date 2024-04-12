/*
 * SettingsController  class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Контролер, що відповідає за обробку подій під час зміни в настройках користувача.
 */
package HealthyDiaryApp.controller;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import HealthyDiaryApp.enums.ActivityLevel;
import HealthyDiaryApp.entity.Ingredients;
import HealthyDiaryApp.entity.User;
import HealthyDiaryApp.calculator.CalorieCalculator;
import HealthyDiaryApp.singleton.ApplicationContext;
import HealthyDiaryApp.util.IngredientComponent;
import HealthyDiaryApp.util.UserComponent;

import java.util.List;

@Slf4j
public class SettingsController {
    private UserComponent userComponent = new UserComponent();
    private IngredientComponent ingredientComponent = new IngredientComponent();
    private boolean ingredientsColumnCreated = false;


    public void addAllergy(VBox allergyVbox, TextField phoneNumberTextField,ImageView plusAllergyBtn,
                           Button updateDataBtn) {
        ComboBox<String> newComboBox = new ComboBox<>();
        double x = 14; // координа Х для ComboBox
        double y = 100;// координа У для ComboBox
        double deltaY = 40; // отсутп

        // Определяем координаты для нового ComboBox
        int numberOfExistingComboBoxes = allergyVbox.getChildren().stream()
                .filter(node -> node instanceof ComboBox)
                .mapToInt(node -> 1)
                .sum();

        y += numberOfExistingComboBoxes * deltaY;

        newComboBox.setLayoutX(x);
        newComboBox.setLayoutY(y);
        newComboBox.setPrefWidth(300);
        newComboBox.setPrefHeight(30);

        newComboBox.setEditable(true);

        List<Ingredients> allIngredients = ingredientComponent.getAllIngredients();
        if (allIngredients != null) {
            for (Ingredients ingredient : allIngredients) {
                newComboBox.getItems().add(ingredient.getNameIngredients());
            }

            allergyVbox.getChildren().add(newComboBox);
            updateDataBtn.setLayoutY(updateDataBtn.getLayoutY() + 35);

            newComboBox.setOnAction(e -> {
                String selectedProduct = newComboBox.getValue();
                Ingredients ingredient = ingredientComponent.findIngredientByName(selectedProduct);
                if (ingredient != null) {
                    userComponent.addUserAllergyForUser(
                            Long.parseLong(phoneNumberTextField.getText()),
                            ingredient.getIdIngredients());
                } else {
                    log.warn("The selected ingredient was not found");
                }
            });
        }
    }

    public void closePane(AnchorPane deleteUserPane) {
        deleteUserPane.setVisible(false);
    }

    public void updateUserData(TextField nameTextField, TextField ageTextField, TextField phoneNumberTextField,
                               TextField heightTextField, TextField weightTextField,
                               ComboBox<ActivityLevel> activityLevelComboBox, CheckBox allergyCheckBox,
                               RadioButton manRadioButton) {
        // Получаем текущего пользователя
        User currentUser = getUserFromApplicationContext();
        if (currentUser != null) {
            if (!nameTextField.getText().isEmpty() &&
                    !ageTextField.getText().isEmpty() &&
                    !phoneNumberTextField.getText().isEmpty() &&
                    !heightTextField.getText().isEmpty() &&
                    !weightTextField.getText().isEmpty()) {

                currentUser.setNameUser(nameTextField.getText());
                currentUser.setAgeUser(Integer.parseInt(ageTextField.getText()));

                // Проверяем, что номер телефона не занят другим пользователем
                if (userComponent.isPhoneNumberExists(Long.parseLong(phoneNumberTextField.getText()))) {
                    ErrorDialogController.showErrorAlert("Помилка", "Цей номер телефона вже зайнятий");
                    return;
                }
                // Преобразуем строку с номером телефона в Long
                Long phoneNumber = null;
                try {
                    phoneNumber = Long.parseLong(phoneNumberTextField.getText());
                } catch (NumberFormatException e) {
                    ErrorDialogController.showErrorAlert("Помилка", "Невірний формат номера телефона");
                    return;
                }
                currentUser.setHeightUser(Double.parseDouble(heightTextField.getText()));
                currentUser.setWeightUser(Double.parseDouble(weightTextField.getText()));

                currentUser.setActivityLevel(activityLevelComboBox.getValue());
                currentUser.setAllergiesUser(allergyCheckBox.isSelected());
                currentUser.setGenderUser(manRadioButton.isSelected());

                double calories = CalorieCalculator.calculateCalories(
                        currentUser.getWeightUser(), currentUser.getHeightUser(),
                        currentUser.getAgeUser(), currentUser.isGenderUser(),
                        currentUser.getActivityLevel());

                double proteins = CalorieCalculator.calculateProtein(calories);
                double fats = CalorieCalculator.calculateFat(calories);
                double carbs = CalorieCalculator.calculateCarbs(calories);


                currentUser.setTotalCaloricUser(calories);
                currentUser.setTotalProteinUser(proteins);
                currentUser.setTotalFatUser(fats);
                currentUser.setTotalCarbsUser(carbs);


                // Обновляем информацию о пользователе в базе данных
                userComponent.updateUser(currentUser);
                ApplicationContext.getInstance().setCurrentUser(
                        userComponent.getUserByPhoneNumber(currentUser.getPhoneNumber()));
            }else {
                ErrorDialogController.showErrorAlert("Помилка","Заповніть всі поля для наступної дії");
                return;

            }
        }
    }


    public void deleteUserAccount(User currentUser) {
        if (currentUser != null) {
            // Удаляем пользователя из базы данных
            userComponent.deleteUserAndRelatedSelectedMenus(currentUser);
            ApplicationContext.getInstance().setCurrentUser(
                    userComponent.getUserByPhoneNumber(currentUser.getPhoneNumber()));
        }
    }

    public User getUserFromApplicationContext() {
        return ApplicationContext.getInstance().getCurrentUser();
    }

    public void deleteAllergy(String allergyName, VBox allergyVbox) {
        User currentUser = getUserFromApplicationContext();
        // Удаление аллергии из базы данных
        userComponent.deleteUserAllergy(currentUser, allergyName);
        for (Node node : allergyVbox.getChildren()) {
            if (node instanceof HBox) {
                HBox hbox = (HBox) node;
                TextField textField = (TextField) hbox.getChildren().get(0);
                if (textField.getText().equals(allergyName)) {
                    allergyVbox.getChildren().remove(hbox);
                    break;
                }
            }
        }
    }


    public void initializeAllergies(VBox allergyVbox, Button updateDataBtn, TextField nameTextField,
                                    TextField ageTextField, TextField phoneNumberTextField,
                                    TextField heightTextField, TextField weightTextField,
                                    ComboBox<ActivityLevel> activityLevelComboBox,
                                    CheckBox allergyCheckBox, RadioButton manRadioButton,
                                    CheckBox causeCheckBox, RadioButton femaleRadioButton) {
        User currentUser = getUserFromApplicationContext();
        if (currentUser != null) {
            List<String> allergyNames = userComponent.getUserAllergiesText(currentUser);
            nameTextField.setText(currentUser.getNameUser());
            phoneNumberTextField.setText(String.valueOf(currentUser.getPhoneNumber()));
            ageTextField.setText(String.valueOf(currentUser.getAgeUser()));
            heightTextField.setText(String.valueOf(currentUser.getHeightUser()));
            weightTextField.setText(String.valueOf(currentUser.getWeightUser()));


            activityLevelComboBox.setValue(currentUser.getActivityLevel());
            allergyCheckBox.setSelected(currentUser.isAllergiesUser());
            causeCheckBox.setSelected(currentUser.isCauseUser());
            if (currentUser.isGenderUser()) {
                manRadioButton.setSelected(true);
            } else {
                femaleRadioButton.setSelected(true);
            }
            if (allergyNames != null) {
                double spacing = 25.0;
                allergyVbox.setSpacing(spacing);

                // Создаем TextField для каждой аллергии и добавляем их на сцену с учетом отступа
                for (String allergyName : allergyNames) {
                    TextField allergyTextField = new TextField(allergyName);
                    Button deleteButton = new Button("Удалить");
                    deleteButton.setOnAction(event -> {
                        deleteAllergy(allergyName, allergyVbox);
                        // Удаляем соответствующий отступ
                        updateDataBtn.setLayoutY(updateDataBtn.getLayoutY() - 25);
                    });

                    HBox hbox = new HBox(allergyTextField, deleteButton);
                    hbox.setSpacing(10);

                    allergyVbox.getChildren().add(hbox);
                }
            }
            updateDataBtn.setLayoutY(updateDataBtn.getLayoutY() + (25 * allergyNames.size()));
        }
    }


}