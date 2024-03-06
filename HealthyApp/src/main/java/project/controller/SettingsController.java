package project.controller;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import project.enums.ActivityLevel;
import project.entity.Ingredients;
import project.entity.User;
import project.calculator.CalorieCalculator;
import project.singleton.ApplicationContext;
import project.util.HibernateMethods;

import java.util.List;

@Slf4j
public class SettingsController {
    private HibernateMethods hibernateMethods = new HibernateMethods();
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

        List<Ingredients> allIngredients = hibernateMethods.getAllIngredients();
        if (allIngredients != null) {
            for (Ingredients ingredient : allIngredients) {
                newComboBox.getItems().add(ingredient.getNameIngredients());
            }

            allergyVbox.getChildren().add(newComboBox);
            updateDataBtn.setLayoutY(updateDataBtn.getLayoutY() + 35);

            newComboBox.setOnAction(e -> {
                String selectedProduct = newComboBox.getValue();
                Ingredients ingredient = hibernateMethods.findIngredientByName(selectedProduct);
                if (ingredient != null) {
                    hibernateMethods.addUserAllergyForUser(
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
                currentUser.setPhoneNumber(Long.parseLong(phoneNumberTextField.getText()));
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
                hibernateMethods.updateUser(currentUser);
                ApplicationContext.getInstance().setCurrentUser(
                        hibernateMethods.getUserByPhoneNumber(currentUser.getPhoneNumber()));
            }
        }
    }


    public void deleteUserAccount(User currentUser) {
        if (currentUser != null) {
            // Удаляем пользователя из базы данных
            hibernateMethods.deleteUserAndRelatedSelectedMenus(currentUser);
            ApplicationContext.getInstance().setCurrentUser(
                    hibernateMethods.getUserByPhoneNumber(currentUser.getPhoneNumber()));
        }
    }

    public User getUserFromApplicationContext() {
        return ApplicationContext.getInstance().getCurrentUser();
    }

    public void deleteAllergy(String allergyName, VBox allergyVbox) {
        User currentUser = getUserFromApplicationContext();
        // Удаление аллергии из базы данных
        hibernateMethods.deleteUserAllergy(currentUser, allergyName);
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
            List<String> allergyNames = hibernateMethods.getUserAllergiesText(currentUser);
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