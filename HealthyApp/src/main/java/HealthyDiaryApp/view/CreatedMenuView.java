package HealthyDiaryApp.view;

import HealthyDiaryApp.controller.CreatedMenuController;
import HealthyDiaryApp.entity.UserSelectedMenu;
import HealthyDiaryApp.model.CustomMenuItem;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import HealthyDiaryApp.navigation.BaseMenuClass;


public class CreatedMenuView extends BaseMenuClass {
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
        initializeButtons(mainPageBtn, calculatorPageBtn, createdMenuPageBtn, changePageBtn);
        initializeMenuButton(loseWeightMenuButton);
        initializeMenuItem(forecastMenuItem);
        AnimationButton.addFadeAnimation(createdMenutBtn);

        createdMenuController.initialize(tableProduct, productsComboBox, checkBoxCreatedPdf, updateBtn, updateImage,
                mainPageBtn, calculatorPageBtn, createdMenuPageBtn, changePageBtn, loseWeightMenuButton,
                forecastMenuItem, statisticsMenuItem, createdMenutBtn, typeMealColumn, nameProductColumn,
                quantityColumn);

    }
}