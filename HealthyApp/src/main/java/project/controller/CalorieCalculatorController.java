package project.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


import javafx.scene.control.*;
import project.method.NavigationMenu;
import project.singleton.ApplicationContext;
import project.entity.Products;
import project.entity.User;
import project.entity.UserSelectedProduct;
import javafx.animation.RotateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import project.util.HibernateMethods;

@Slf4j
public class CalorieCalculatorController {

    private HibernateMethods hibernateMethods = new HibernateMethods();

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
    void Handle(MouseEvent event) {
        PaneAddProduct.setVisible(true);
        addNewProductBtn.setOnAction(actionEvent -> {
            String name = nameProductField.getText();
            double calorie = Double.parseDouble(calorieProductField.getText());
            double fat = Double.parseDouble(fatproductField.getText());
            double protein = Double.parseDouble(proteinProductField.getText());
            double carbs = Double.parseDouble(carbsProductsField.getText());
            hibernateMethods.addNewProducts(name,calorie,fat,protein,carbs);
            PaneAddProduct.setVisible(false);

        });
        closePane.setOnMouseClicked(this::ClosePane);
    }

    @FXML
    void ClosePane(MouseEvent event) {
        PaneAddProduct.setVisible(false);
    }


    @FXML
    void initialize() {
        User user = getUserFromApplicationContext();
        updateCaloriesFields(user);

        List<UserSelectedProduct> userSelectedProducts = hibernateMethods.getUserSelectedProductsForNumberPhone(
                user.getPhoneNumber()
        );
        if (userSelectedProducts != null) {
            tableProduct.getItems().addAll(userSelectedProducts);
        }
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
        List<Products> allProduct = hibernateMethods.getAllProduct();
        if (allProduct != null) {
            for (Products products : allProduct) {
                productsComboBox.getItems().add(products.getNameProduct());
            }
        }

        addProductBtn.setOnAction(actionEvent -> {
            double grams = Double.parseDouble(gramsField.getText());
            String selectedProduct = productsComboBox.getValue();
            if (selectedProduct != null && !selectedProduct.isEmpty()) {
                Products product = hibernateMethods.findProductByName(selectedProduct);

                if (product != null) {
                    if (user != null) {
                        addProductAndUpdateUserValues(user, product, grams);
                        updateTableContent();
                        updateCaloriesFields(user); // Обновлення полів
                    } else {
                       log.warn("the selected user was not found");
                    }
                } else {
                    log.warn("the selected product was not found");
                }
            }
        });

        updateBtn.setOnAction(actionEvent -> {
            hibernateMethods.DropProductUser(user.getPhoneNumber());
            rotateImage();
            updateTableContent();
            updateCaloriesFields(user);

        });
        plusProduct.setOnMouseClicked(this::Handle);

        mainPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("mainpage"));
        calculatorPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("calorieCalculator"));
        createdMenuPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("createdMenu"));
        changePageBtn.setOnAction(event -> NavigationMenu.navigateToPage("settings"));
        loseWeightMenuButton.setOnAction(event -> NavigationMenu.navigateToPage("loseWeight"));
    }
    private User getUserFromApplicationContext() {
        return ApplicationContext.getInstance().getCurrentUser();
    }
    private void rotateImage() {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), updateImage);
        rotateTransition.setByAngle(360);
        rotateTransition.play();
    }

    private void updateTableContent() {
        tableProduct.getItems().clear();
        User user = getUserFromApplicationContext();
        List<UserSelectedProduct> userSelectedProducts = hibernateMethods.getUserSelectedProductsForNumberPhone(
                user.getPhoneNumber()
        );
        if (userSelectedProducts != null) {
            tableProduct.getItems().addAll(userSelectedProducts);
        }
    }

    private void updateCaloriesFields(User user) {
        double totalCaloriesFromProducts = 0;
        double totalFatFromProducts = 0;
        double totalProteinFromProducts = 0;
        double totalCarbsFromProducts= 0;

           // hibernateMethods.clearSessionCache();
            totalCaloriesFromProducts = hibernateMethods.getTotalCaloriesForUser(user.getPhoneNumber());
            totalFatFromProducts = hibernateMethods.getTotalFatForUser(user.getPhoneNumber());
            totalProteinFromProducts = hibernateMethods.getTotalProteinForUser(user.getPhoneNumber());
            totalCarbsFromProducts = hibernateMethods.getTotalCarbsForUser(user.getPhoneNumber());

        if (totalCaloriesFromProducts > 0) {
            caloriesField.setText(String.format("%.1f", totalCaloriesFromProducts));
            fatField.setText(String.format("%.1f", totalFatFromProducts));
            proteinField.setText(String.format("%.1f", totalProteinFromProducts));
            carbsField.setText(String.format("%.1f", totalCarbsFromProducts));
        }else{
            caloriesField.setText(String.format("%.1f", user.getTotalCaloricUser()));
            fatField.setText(String.format("%.1f", user.getTotalFatUser()));
            proteinField.setText(String.format("%.1f", user.getTotalProteinUser()));
            carbsField.setText(String.format("%.1f", user.getTotalCarbsUser()));
        }
    }

    private void addProductAndUpdateUserValues(User users, Products product, double grams) {
        double caloriesToAdd = (product.getCaloriesProducts() / 100) * grams;
        double fatToAdd = (product.getFatProducts() / 100) * grams;
        double proteinToAdd = (product.getProteinProducts() / 100) * grams;
        double carbsToAdd = (product.getCarbsProducts() / 100) * grams;

        List<UserSelectedProduct> userSelectedProducts = hibernateMethods.getUserSelectedProductsForNumberPhone(
                users.getPhoneNumber());

        if (userSelectedProducts != null && !userSelectedProducts.isEmpty()) {

            UserSelectedProduct lastSelectedProduct = userSelectedProducts.get(userSelectedProducts.size() - 1);


            double remainingCalories =  lastSelectedProduct.getCaloriesUserSelectedProduct();
            double remainingFat = lastSelectedProduct.getFatUserSelectedProduct();
            double remainingProtein = lastSelectedProduct.getProteinUserSelectedProduct();
            double remainingCarbs =  lastSelectedProduct.getCarbsUserSelectedProduct();


            remainingCalories -= caloriesToAdd;
            remainingFat -= fatToAdd;
            remainingProtein -= proteinToAdd;
            remainingCarbs -= carbsToAdd;
            hibernateMethods.addUserSelectedProduct(users.getPhoneNumber(),
                    product.getProductId(), grams, remainingCalories,
                    remainingFat, remainingProtein, remainingCarbs);
        } else {
            double remainingCalories = users.getTotalCaloricUser() - caloriesToAdd;
            double remainingFat = users.getTotalFatUser() - fatToAdd;
            double remainingProtein = users.getTotalProteinUser() - proteinToAdd;
            double remainingCarbs = users.getTotalCarbsUser() - carbsToAdd;

            hibernateMethods.addUserSelectedProduct(users.getPhoneNumber(),
                    product.getProductId(), grams, remainingCalories,
                    remainingFat, remainingProtein, remainingCarbs);
        }
    }
}
