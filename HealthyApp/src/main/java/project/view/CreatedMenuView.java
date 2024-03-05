package project.view;

import project.controller.CreatedMenuController;
import project.entity.UserSelectedMenu;
import project.method.NavigationMenu;
import project.tableView.CustomMenuItem;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;


public class CreatedMenuView {
    private  CreatedMenuController createdMenuController = new CreatedMenuController();
    @FXML
    private ImageView updateImage;

    @FXML
    private Button updateBtn;

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
    private ComboBox<String> productsComboBox;

    @FXML
    private Button createdMenutBtn;

    @FXML
    private CheckBox checkBoxCreatedPdf;

    @FXML
    private TableView<CustomMenuItem> tableProduct;

    @FXML
    private TableColumn<UserSelectedMenu, String> typeMealColumn;

    @FXML
    private TableColumn<UserSelectedMenu, String> nameProductColumn;

    @FXML
    private TableColumn<UserSelectedMenu, Double> quantityColumn;



    @FXML
    void initialize() {
        createdMenuController.initialize(tableProduct, productsComboBox, checkBoxCreatedPdf, updateBtn, updateImage,
                mainPageBtn, calculatorPageBtn, createdMenuPageBtn, changePageBtn, loseWeightMenuButton,
                forecastMenuItem, statisticsMenuItem, createdMenutBtn, typeMealColumn, nameProductColumn,
                quantityColumn);

        mainPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("mainpage"));
        calculatorPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("calorieCalculator"));
        createdMenuPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("createdMenu"));
        changePageBtn.setOnAction(event -> NavigationMenu.navigateToPage("settings"));
        loseWeightMenuButton.setOnAction(event -> NavigationMenu.navigateToPage("loseWeight"));
        forecastMenuItem.setOnAction(event -> NavigationMenu.navigateToPage("forecast"));

    }
}