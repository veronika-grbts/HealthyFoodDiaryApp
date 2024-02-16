import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import Method.CalorieCalculator;
import entity.ActivityLevel;
import entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import util.HibernateMethods;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

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
    private ComboBox<String> signUpGender;

    @FXML
    private ComboBox<ActivityLevel> signUpActivityLevel;

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

    private ComboBox<String> additionalComboBox;

    @FXML
    void Handle(MouseEvent event) {
        // Вызываем метод, который добавляет поле
        addAdditionalField();
    }
    @FXML
    void initialize() {

        signUpActivityLevel.getItems().addAll(ActivityLevel.High, ActivityLevel.Medium, ActivityLevel.Low);
        signUpGender.getItems().addAll("чоловік", "жінка");
        ObservableList<String> data = signUpGender.getItems();
                signUpGender.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                signUpGender.setItems(data); // Показать все элементы списка, если поле пустое
                signUpGender.hide(); // Скрыть выпадающий список
            } else {
                ObservableList<String> filteredData = FXCollections.observableArrayList();
                for (String item : data) {
                    if (item.toLowerCase().contains(newValue.toLowerCase())) {
                        filteredData.add(item);
                    }
                }
                signUpGender.setItems(filteredData); // Показать только отфильтрованные элементы списка
                signUpGender.show(); // Показать выпадающий список
            }
        });

        nextBtn1.setOnAction(event -> {
            signUpPane1.setVisible(false);
            signUpPane2.setVisible(true);

        });


        nextBtn2.setOnAction(event -> {
            signUpPane2.setVisible(false);
            signUpPane3.setVisible(true);


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

            double calories = CalorieCalculator.calculateCalories(weight, height, age, gender, activityLevel);
            double protein = CalorieCalculator.calculateProtein(calories);
            double fat = CalorieCalculator.calculateFat(calories);
            double carbs = CalorieCalculator.calculateCarbs(calories);

            hibernateMethods.createUser(Long.parseLong(phone), name, age, weight, height, gender, activityLevel, hasAllergy, hasCause, calories, protein, fat, carbs);

            // Переход на главную страницу с данными нового пользователя
            User newUser = hibernateMethods.getUserInfo(Long.parseLong(phone));
            try {
                HibbernateRunner.setRoot("mainpage", newUser);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    private void addAdditionalField() {
        additionalComboBox = new ComboBox<>();
        // Устанавливаем параметры ComboBox
        // Например, вы можете добавить элементы в ComboBox
        additionalComboBox.getItems().addAll("Option 1", "Option 2", "Option 3");
        // Устанавливаем размеры ComboBox
        additionalComboBox.setPrefWidth(300);
        additionalComboBox.setPrefHeight(30);
        // Устанавливаем позицию ComboBox
        additionalComboBox.setLayoutX(14);
        additionalComboBox.setLayoutY(100);
        // Добавляем ComboBox на signUpPane3
        signUpPane3.getChildren().add(additionalComboBox);
    }
}
