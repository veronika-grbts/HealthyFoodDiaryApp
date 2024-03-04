package project.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import project.entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import project.method.NavigationMenu;

public class MainPageController {

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
    private TextField name_id;

    @FXML
    private TextField numberPhone;

    @FXML
    private TextField age;

    @FXML
    private TextField height;

    @FXML
    private TextField weight;

    @FXML
    private TextField gender;

    @FXML
    private TextField allergy;

    public void fillUserData(User user) {
        name_id.setText(user.getNameUser());
        numberPhone.setText(String.valueOf(user.getPhoneNumber()));
        age.setText(String.valueOf(user.getAgeUser()));
        height.setText(String.valueOf(user.getHeightUser()) +" см");
        weight.setText(String.valueOf(user.getWeightUser()) + " кг");
        gender.setText(user.isGenderUser() ? "чоловік" : "жінка");
        allergy.setText(user.isAllergiesUser() ? "так" : "ні");
    }
    @FXML
    void initialize() {
        mainPageBtn.setOnAction(event -> {
            NavigationMenu.navigateToPage("mainpage");
        });
        calculatorPageBtn.setOnAction(event -> {
            NavigationMenu.navigateToPage("calorieCalculator");
        });
        createdMenuPageBtn.setOnAction(event -> {
            NavigationMenu.navigateToPage("createdMenu");
        });
        changePageBtn.setOnAction(event -> {
            NavigationMenu.navigateToPage("settings");
        });
        loseWeightMenuButton.setOnAction(event -> {
            NavigationMenu.navigateToPage("loseWeight");
        });
    }

}


