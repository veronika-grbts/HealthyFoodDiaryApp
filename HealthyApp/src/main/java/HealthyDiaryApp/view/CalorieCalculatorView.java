package HealthyDiaryApp.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


import HealthyDiaryApp.controller.ErrorDialogController;
import HealthyDiaryApp.navigation.MouseEnterHandler;
import javafx.scene.Parent;
import javafx.scene.control.*;
import HealthyDiaryApp.controller.CalorieCalculatorController;
import HealthyDiaryApp.entity.Products;
import HealthyDiaryApp.entity.User;
import HealthyDiaryApp.entity.UserSelectedProduct;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import HealthyDiaryApp.navigation.BaseMenuClass;
import HealthyDiaryApp.util.ProductComponent;
/*
 * CalorieCalculatorView class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Цей клас представляє відображення створення сторінки "лічильник калорій" і наслідує клас BaseMenuClass.
 * Клас містить різні поля та методи для взаємодії з елементами вікна та обробки подій кнопок.
 */

@Slf4j
public class CalorieCalculatorView extends BaseMenuClass {
    private  CalorieCalculatorController calorieCalculatorController = new CalorieCalculatorController();
    private ProductComponent productComponent = new ProductComponent();
    @FXML
    private ImageView updateImage;

    @FXML
    private Button updateBtn;

    @FXML
    private TextField fatField;

    @FXML
    private TextField proteinField;

    @FXML
    private TextField carbsField;

    @FXML
    private TextField caloriesField;

    @FXML
    private ComboBox<String> productsComboBox;

    @FXML
    private Button addProductBtn;

    @FXML
    private TextField gramsField;

    @FXML
    private ImageView plusProduct;
    @FXML
    private TableView<UserSelectedProduct> tableProduct;

    @FXML
    private TableColumn<UserSelectedProduct, String> nameProduct;

    @FXML
    private TableColumn<UserSelectedProduct, Double> quantity;

    @FXML
    private AnchorPane PaneAddProduct;

    @FXML
    private TextField nameProductField;

    @FXML
    private TextField calorieProductField;

    @FXML
    private TextField proteinProductField;

    @FXML
    private TextField fatproductField;

    @FXML
    private TextField carbsProductsField;

    @FXML
    private Button addNewProductBtn;

    @FXML
    private ImageView closePane;

    @FXML
    private ImageView closeAppImg;

    @FXML
    private ImageView MinimizeAppImg;

    @FXML
    private Button createdMenuPageBtn;

    @FXML
    private Button loseWeightMenuButton;

    @FXML
    private Button changePageBtn;

    @FXML
    private Button progresisMenuItem;

    @FXML
    private Button forecastMenuItem;

    @FXML
    private Button calculatorPageBtn;

    @FXML
    private Button mainPageBtn;

    @FXML
    private ImageView maximizeAppImg;

    // Метод для установки обработчиков событий наведения и убирания мыши с кнопки loseWeightMenuButton
    private void setMouseHandlers() {
        MouseEnterHandler.addMouseEnterHandler(loseWeightMenuButton, forecastMenuItem, progresisMenuItem, changePageBtn);
    }

    @FXML
    void initialize() {
        initializeButtons(mainPageBtn, calculatorPageBtn, createdMenuPageBtn, changePageBtn, loseWeightMenuButton, forecastMenuItem, progresisMenuItem);
        setMouseHandlers(); // Установить обработчики событий наведения и убирания мыши
        AnimationButton.addFadeAnimation(addProductBtn);
        AnimationButton.addFadeAnimation(addNewProductBtn);


        User user = calorieCalculatorController.getUserFromApplicationContext();
        calorieCalculatorController.updateCaloriesFields(user, caloriesField, fatField, proteinField, carbsField);
        calorieCalculatorController.updateTableContent(tableProduct);

        // Обработчик для закрытия приложения при нажатии на closeAppImg
        closeAppImg.setOnMouseClicked(event -> {
            // Получаем сцену и закрываем ее
            Stage stages = (Stage) closeAppImg.getScene().getWindow();
            stages.close();
        });

        // Обработчик для сворачивания окна при нажатии на MinimizeAppImg
        MinimizeAppImg.setOnMouseClicked(event -> {
            // Получаем сцену и минимизируем окно
            Stage stages = (Stage) MinimizeAppImg.getScene().getWindow();
            stages.setIconified(true);
        });

        maximizeAppImg.setOnMouseClicked(event -> {
            Stage stage = (Stage) maximizeAppImg.getScene().getWindow();
            if (stage.isFullScreen()) {
                stage.setFullScreen(false);
                stage.setWidth(1360); // Устанавливаем ширину окна
                stage.setHeight(720); // Устанавливаем высоту окна

            } else {
                stage.setFullScreen(true);
            }
        });


        nameProduct.setCellValueFactory(cellData -> {
            UserSelectedProduct userSelectedProduct = cellData.getValue();
            Products product = userSelectedProduct.getProducts();
            if (product != null) {
                return new SimpleStringProperty(product.getNameProduct());
            } else {
                return new SimpleStringProperty("");
            }
        });

        quantity.setCellValueFactory(new PropertyValueFactory<>("gramsUserSelectedProduct"));

        List<Products> allProduct = productComponent.getAllProduct();
        if (allProduct != null) {
            for (Products products : allProduct) {
                productsComboBox.getItems().add(products.getNameProduct());
            }
        }

        addProductBtn.setOnAction(actionEvent -> {
            String gramsText = gramsField.getText();
            if (!gramsText.isEmpty()) {
                Double grams = Double.parseDouble(gramsText);
                String selectedProduct = productsComboBox.getValue();
                if (selectedProduct != null && !selectedProduct.isEmpty()) {
                    Products product = productComponent.findProductByName(selectedProduct);
                    if (product != null) {
                        if (user != null) {
                            calorieCalculatorController.addProductAndUpdateUserValues(user, product, grams);
                            calorieCalculatorController.updateTableContent(tableProduct);
                            calorieCalculatorController.updateCaloriesFields(user, caloriesField, fatField, proteinField, carbsField);
                        } else {
                            log.warn("the selected user was not found");
                        }
                    } else {
                        ErrorDialogController.showErrorAlert("Помилка при вводі даних", "Перевірте введені Вами дані. Деякі поля пусті");
                        log.warn("the selected product was not found");
                    }
                } else {
                    ErrorDialogController.showErrorAlert("Помилка", "Ви не обрали продукт, який бажаєте додати");
                }
            } else {
                ErrorDialogController.showErrorAlert("Помилка при вводі даних", "Введіть кількість продукту");
            }
        });


        updateBtn.setOnAction(actionEvent -> {
            calorieCalculatorController.dropProductUser();
            calorieCalculatorController.rotateImage(updateImage);
            calorieCalculatorController.updateTableContent(tableProduct);
            calorieCalculatorController.updateCaloriesFields(user, caloriesField, fatField, proteinField, carbsField);
        });

        plusProduct.setOnMouseClicked(this::Handle);
    }
    @FXML
    void Handle(MouseEvent event) {
        PaneAddProduct.setVisible(true);
        addNewProductBtn.setOnAction(actionEvent -> {
            String name = nameProductField.getText();
            String calorieText = calorieProductField.getText();
            String fatText = fatproductField.getText();
            String proteinText = proteinProductField.getText();
            String carbsText = carbsProductsField.getText();

            // Проверка на правильность введенных данных
            boolean isCalorieValid = TextFieldValidator.isWeightValid(calorieText);
            boolean isFatValid = TextFieldValidator.isWeightValid(fatText);
            boolean isProteinValid = TextFieldValidator.isWeightValid(proteinText);
            boolean isCarbsValid = TextFieldValidator.isWeightValid(carbsText);
            boolean isNameValid = TextFieldValidator.isValidField(nameProductField);
            // Установка стиля для полей ввода
            if (!isCalorieValid) {
                TextFieldValidator.setInvalidStyle(calorieProductField);
            } else {
                TextFieldValidator.setValidStyle(calorieProductField);
            }

            if (!isFatValid) {
                TextFieldValidator.setInvalidStyle(fatproductField);
            } else {
                TextFieldValidator.setValidStyle(fatproductField);
            }

            if (!isProteinValid) {
                TextFieldValidator.setInvalidStyle(proteinProductField);
            } else {
                TextFieldValidator.setValidStyle(proteinProductField);
            }

            if (!isCarbsValid) {
                TextFieldValidator.setInvalidStyle(carbsProductsField);
            } else {
                TextFieldValidator.setValidStyle(carbsProductsField);
            }

            if (!isNameValid) {
                TextFieldValidator.setInvalidStyle(nameProductField);
            } else {
                TextFieldValidator.setValidStyle(nameProductField);
            }

            // Если все данные введены правильно, то продолжаем выполнение действий
            if (isCalorieValid && isFatValid && isProteinValid && isCarbsValid) {
                double calorie = Double.parseDouble(calorieText);
                double fat = Double.parseDouble(fatText);
                double protein = Double.parseDouble(proteinText);
                double carbs = Double.parseDouble(carbsText);

                productComponent.addNewProducts(name, calorie, fat, protein, carbs);
                PaneAddProduct.setVisible(false);
            } else {
                // Вывести сообщение об ошибке или выполнить другие действия, если введены неправильные данные
                ErrorDialogController.showErrorAlert("Помилка при вводі даних", "Будь-ласка введіть коректний формат для даних.");
            }
        });
        closePane.setOnMouseClicked(this::ClosePane);
    }


    @FXML
    void ClosePane(MouseEvent event) {
        PaneAddProduct.setVisible(false);
    }
}