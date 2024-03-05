package project.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import project.controller.MainPageController;
import project.method.NavigationMenu;

public class MainPageView {
    private MainPageController mainPageController = new MainPageController();
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

    @FXML
    void initialize() {

        mainPageController.fillUserData(name_id, numberPhone, age,
                height, weight, gender,allergy);


        mainPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("mainpage"));
        calculatorPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("calorieCalculator"));
        createdMenuPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("createdMenu"));
        changePageBtn.setOnAction(event -> NavigationMenu.navigateToPage("settings"));
        loseWeightMenuButton.setOnAction(event -> NavigationMenu.navigateToPage("loseWeight"));
        forecastMenuItem.setOnAction(event -> NavigationMenu.navigateToPage("forecast"));
    }

}

