package HealthyDiaryApp.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


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
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    private TableView<UserSelectedProduct> tableProduct;

    @FXML
    private TableColumn<UserSelectedProduct, String> nameProduct;

    @FXML
    private TableColumn<UserSelectedProduct, Double> quantity;

    @FXML
    private ImageView updateImage;

    @FXML
    private Button updateBtn;

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
    private ImageView closePane;

    @FXML
    private Button addNewProductBtn;

    @FXML
    private ImageView plusProduct;



    @FXML
    void initialize() {
        initializeButtons(mainPageBtn, calculatorPageBtn, createdMenuPageBtn, changePageBtn);
        initializeMenuButton(loseWeightMenuButton);
        initializeMenuItem(forecastMenuItem);
        AnimationButton.addFadeAnimation(addProductBtn);
        AnimationButton.addFadeAnimation(addNewProductBtn);

        User user = calorieCalculatorController.getUserFromApplicationContext();
        calorieCalculatorController.updateCaloriesFields(user, caloriesField, fatField, proteinField, carbsField);
        calorieCalculatorController.updateTableContent(tableProduct);

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
            double grams = Double.parseDouble(gramsField.getText());
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
                    log.warn("the selected product was not found");
                }
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
            double calorie = Double.parseDouble(calorieProductField.getText());
            double fat = Double.parseDouble(fatproductField.getText());
            double protein = Double.parseDouble(proteinProductField.getText());
            double carbs = Double.parseDouble(carbsProductsField.getText());
            productComponent.addNewProducts(name, calorie, fat, protein, carbs);
            PaneAddProduct.setVisible(false);
        });
        closePane.setOnMouseClicked(this::ClosePane);
    }

    @FXML
    void ClosePane(MouseEvent event) {
        PaneAddProduct.setVisible(false);
    }
}