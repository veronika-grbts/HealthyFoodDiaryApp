package HealthyDiaryApp.controller;

import javafx.animation.RotateTransition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import HealthyDiaryApp.entity.Products;
import HealthyDiaryApp.entity.User;
import HealthyDiaryApp.entity.UserSelectedProduct;
import HealthyDiaryApp.singleton.ApplicationContext;
import HealthyDiaryApp.util.UserComponent;
import HealthyDiaryApp.util.UserProductComponent;

import java.util.List;

public class CalorieCalculatorController {
    private static final int TURN_IMAGE = 360;
    private UserComponent userComponent = new UserComponent();
    private UserProductComponent userSelectedProduct = new UserProductComponent();

    public void updateTableContent(TableView<UserSelectedProduct> tableProduct) {
        tableProduct.getItems().clear();
        User user = getUserFromApplicationContext();
        List<UserSelectedProduct> userSelectedProducts = userSelectedProduct.getUserSelectedProductsForNumberPhone(
                user.getPhoneNumber()
        );
        if (userSelectedProducts != null) {
            tableProduct.getItems().addAll(userSelectedProducts);
        }
    }

    public void updateCaloriesFields(User user, TextField caloriesField,
                                     TextField fatField, TextField proteinField,
                                     TextField carbsField) {
        double totalCaloriesFromProducts = userSelectedProduct.getTotalCaloriesForUser(user.getPhoneNumber());
        double totalFatFromProducts = userSelectedProduct.getTotalFatForUser(user.getPhoneNumber());
        double totalProteinFromProducts = userSelectedProduct.getTotalProteinForUser(user.getPhoneNumber());
        double totalCarbsFromProducts = userSelectedProduct.getTotalCarbsForUser(user.getPhoneNumber());

        if (totalCaloriesFromProducts > 0) {
            caloriesField.setText(String.format("%.1f", totalCaloriesFromProducts));
            fatField.setText(String.format("%.1f", totalFatFromProducts));
            proteinField.setText(String.format("%.1f", totalProteinFromProducts));
            carbsField.setText(String.format("%.1f", totalCarbsFromProducts));
        } else {
            caloriesField.setText(String.format("%.1f", user.getTotalCaloricUser()));
            fatField.setText(String.format("%.1f", user.getTotalFatUser()));
            proteinField.setText(String.format("%.1f", user.getTotalProteinUser()));
            carbsField.setText(String.format("%.1f", user.getTotalCarbsUser()));
        }
    }

    public void addProductAndUpdateUserValues(User users, Products product, double grams) {
        double caloriesToAdd = (product.getCaloriesProducts() / 100) * grams;
        double fatToAdd = (product.getFatProducts() / 100) * grams;
        double proteinToAdd = (product.getProteinProducts() / 100) * grams;
        double carbsToAdd = (product.getCarbsProducts() / 100) * grams;

        List<UserSelectedProduct> userSelectedProducts = userSelectedProduct.getUserSelectedProductsForNumberPhone(
                users.getPhoneNumber()
        );

        if (userSelectedProducts != null && !userSelectedProducts.isEmpty()) {
            UserSelectedProduct lastSelectedProduct = userSelectedProducts.get(userSelectedProducts.size() - 1);

            double remainingCalories = lastSelectedProduct.getCaloriesUserSelectedProduct() - caloriesToAdd;
            double remainingFat = lastSelectedProduct.getFatUserSelectedProduct() - fatToAdd;
            double remainingProtein = lastSelectedProduct.getProteinUserSelectedProduct() - proteinToAdd;
            double remainingCarbs = lastSelectedProduct.getCarbsUserSelectedProduct() - carbsToAdd;

            userComponent.addUserSelectedProduct(users.getPhoneNumber(),
                    product.getProductId(), grams, remainingCalories,
                    remainingFat, remainingProtein, remainingCarbs);
        } else {
            double remainingCalories = users.getTotalCaloricUser() - caloriesToAdd;
            double remainingFat = users.getTotalFatUser() - fatToAdd;
            double remainingProtein = users.getTotalProteinUser() - proteinToAdd;
            double remainingCarbs = users.getTotalCarbsUser() - carbsToAdd;

            userComponent.addUserSelectedProduct(users.getPhoneNumber(),
                    product.getProductId(), grams, remainingCalories,
                    remainingFat, remainingProtein, remainingCarbs);
        }
    }

    public void rotateImage(ImageView updateImage) {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), updateImage);
        rotateTransition.setByAngle(TURN_IMAGE);
        rotateTransition.play();
    }

    public void dropProductUser(){
        User user = getUserFromApplicationContext();
        userSelectedProduct.DropProductUser(user.getPhoneNumber());
    }
    public User getUserFromApplicationContext() {
        return ApplicationContext.getInstance().getCurrentUser();
    }
}