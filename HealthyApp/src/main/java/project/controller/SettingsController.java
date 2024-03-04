package project.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import project.HibbernateRunner;
import project.method.CalorieCalculator;
import project.method.NavigationMenu;
import project.singleton.ApplicationContext;
import project.entity.ActivityLevel;
import project.entity.Ingredients;
import project.entity.User;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import project.util.HibernateMethods;

@Slf4j
public class SettingsController {
    private HibernateMethods hibernateMethods = new HibernateMethods();
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button deleteUserBtn;

    @FXML
    private ImageView deleteImage;

    @FXML
    private Button mainPageBtn;

    @FXML
    private Button calculatorPageBtn;

    @FXML
    private Button createdMenuPageBtn;

    @FXML
    private Button changePageBtn;

    @FXML
    private SplitMenuButton loseWeightMenuButton;

    @FXML
    private MenuItem forecastMenuItem;

    @FXML
    private MenuItem statisticsMenuItem;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField ageTextField;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private TextField heightTextField;

    @FXML
    private TextField weightTextField;

    @FXML
    private ComboBox<ActivityLevel> activityLevelComboBox;

    @FXML
    private CheckBox causeCheckBox;

    @FXML
    private RadioButton manRadioButton;

    @FXML
    private RadioButton femaleRadioButton;

    @FXML
    private Button updateDataBtn;

    @FXML
    private CheckBox allergyCheckBox;

    @FXML
    private VBox allergyVbox;

    @FXML
    private ImageView plusAllergyBtn;

    @FXML
    private AnchorPane deleteUserPane;

    @FXML
    private ImageView plusCancleImg;

    @FXML
    private Button cancelDeleteBtn;

    @FXML
    private Button deleteUserAccountBtn;

    @FXML
    void AddAllergy(MouseEvent event) {
            ComboBox<String> newComboBox = new ComboBox<>();
            double x = 14; // координа Х для ComboBox
            double y = 100;// координа У для ComboBox
            double deltaY = 40; // отсутп

            // Определяем координаты для нового ComboBox
            int numberOfExistingComboBoxes = allergyVbox.getChildren().stream()
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

                allergyVbox.getChildren().add(newComboBox);
                // Увеличиваем местоположение кнопки updateDataBtn по оси Y на 35 единиц
                updateDataBtn.setLayoutY(updateDataBtn.getLayoutY() + 35);

                // Добавляем слушатель события для выбора элемента в новом ComboBox
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

    @FXML
    void ClosePane(MouseEvent event) {
        deleteUserPane.setVisible(false);
    }
        @FXML
    void initialize() {
            ToggleGroup group = new ToggleGroup();
            manRadioButton.setToggleGroup(group);
            femaleRadioButton.setToggleGroup(group);
            plusAllergyBtn.setOnMouseClicked(this::AddAllergy);
            // Получаем текущего пользователя
            User currentUser = getUserFromApplicationContext();
            if (currentUser != null) {
                // Заполняем поля информацией о пользователе
                List<String> allergyNames = hibernateMethods.getUserAllergiesText(currentUser);
                nameTextField.setText(currentUser.getNameUser());
                phoneNumberTextField.setText(String.valueOf(currentUser.getPhoneNumber()));
                ageTextField.setText(String.valueOf(currentUser.getAgeUser()));
                heightTextField.setText(String.valueOf(currentUser.getHeightUser()));
                weightTextField.setText(String.valueOf(currentUser.getWeightUser()));

                // Устанавливаем значения ComboBox, CheckBox и RadioButton на основе информации о пользователе
                activityLevelComboBox.setValue(currentUser.getActivityLevel());
                allergyCheckBox.setSelected(currentUser.isAllergiesUser());
                causeCheckBox.setSelected(currentUser.isCauseUser());
                if (currentUser.isGenderUser()) {
                    manRadioButton.setSelected(true);
                } else {
                    femaleRadioButton.setSelected(true);
                }
                activityLevelComboBox.getItems().addAll(ActivityLevel.High, ActivityLevel.Medium, ActivityLevel.Low);

                if (allergyNames != null) {
                    // Устанавливаем значения для отступов между элементами в VBox
                    double spacing = 25.0;
                    allergyVbox.setSpacing(spacing);

                    // Создаем TextField для каждой аллергии и добавляем их на сцену с учетом отступа
                    for (String allergyName : allergyNames) {
                        TextField allergyTextField = new TextField(allergyName);
                        Button deleteButton = new Button("Удалить");
                        deleteButton.setOnAction(event -> {
                            deleteAllergy(allergyTextField.getText());
                            updateDataBtn.setLayoutY(updateDataBtn.getLayoutY() - (25 * allergyNames.size()));
                        });

                        HBox hbox = new HBox(allergyTextField, deleteButton);
                        hbox.setSpacing(10);

                        allergyVbox.getChildren().add(hbox);
                    }
                }

                // Увеличиваем местоположение кнопки updateDataBtn по оси Y
                updateDataBtn.setLayoutY(updateDataBtn.getLayoutY() + (25 * allergyNames.size()));
            }

            // Обработчик события для кнопки updateDataBtn
            updateDataBtn.setOnAction(event -> updateUserData());

            deleteUserBtn.setOnAction(event -> {
                deleteUserPane.setVisible(true);
                plusCancleImg.setOnMouseClicked(this::ClosePane);
            });
            cancelDeleteBtn.setOnAction(event -> deleteUserPane.setVisible(false));

            deleteUserAccountBtn.setOnAction(event -> {
                if (currentUser != null) {
                    // Удаляем пользователя из базы данных
                    hibernateMethods.deleteUserAndRelatedSelectedMenus(currentUser);
                    ApplicationContext.getInstance().setCurrentUser(
                            hibernateMethods.getUserByPhoneNumber(currentUser.getPhoneNumber()));
                }
                try {
                    HibbernateRunner.setRoot("primary");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            mainPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("mainpage"));
            calculatorPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("calorieCalculator"));
            createdMenuPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("createdMenu"));
            changePageBtn.setOnAction(event -> NavigationMenu.navigateToPage("settings"));
            loseWeightMenuButton.setOnAction(event -> NavigationMenu.navigateToPage("loseWeight"));
        }

    private User getUserFromApplicationContext() {
        return ApplicationContext.getInstance().getCurrentUser();
    }

    private void updateUserData() {
        // Получаем текущего пользователя
        User currentUser = getUserFromApplicationContext();
        if (currentUser != null) {
            // Обновляем данные пользователя на основе введенных значений в полях
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

            // Обновляем данные пользователя
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

    private void deleteAllergy(String allergyName) {
        User currentUser = getUserFromApplicationContext();
        // Удаление аллергии из базы данных
        hibernateMethods.deleteUserAllergy(currentUser, allergyName);
        // Удаление аллергии из интерфейса
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
}

