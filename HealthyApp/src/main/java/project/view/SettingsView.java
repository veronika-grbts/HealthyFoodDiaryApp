package project.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import project.HibbernateRunner;
import project.controller.SettingsController;
import project.method.NavigationMenu;
import project.entity.ActivityLevel;
import project.entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SettingsView {
    private SettingsController settingsController = new SettingsController();

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
        settingsController.addAllergy(allergyVbox, phoneNumberTextField, plusAllergyBtn, updateDataBtn);
    }

    @FXML
    void ClosePane(MouseEvent event) {
        settingsController.closePane(deleteUserPane);
    }

    @FXML
    void initialize() {
        ToggleGroup group = new ToggleGroup();
        manRadioButton.setToggleGroup(group);
        femaleRadioButton.setToggleGroup(group);
        plusAllergyBtn.setOnMouseClicked(this::AddAllergy);
        User currentUser = settingsController.getUserFromApplicationContext();

        settingsController.initializeAllergies( allergyVbox,  updateDataBtn,  nameTextField,
                 ageTextField, phoneNumberTextField, heightTextField,
                 weightTextField, activityLevelComboBox, allergyCheckBox,
                manRadioButton, causeCheckBox, femaleRadioButton);

        // Обработчики событий для кнопок
        deleteUserBtn.setOnAction(event -> {
            deleteUserPane.setVisible(true);
            plusCancleImg.setOnMouseClicked(this::ClosePane);
        });

        updateDataBtn.setOnAction(event -> {
            settingsController.updateUserData(nameTextField, ageTextField, phoneNumberTextField,
                    heightTextField, weightTextField, activityLevelComboBox, allergyCheckBox,
                    manRadioButton);
        });


        cancelDeleteBtn.setOnAction(event -> deleteUserPane.setVisible(false));

        deleteUserAccountBtn.setOnAction(event -> {
            settingsController.deleteUserAccount(currentUser);
            try {
                HibbernateRunner.setRoot("primary");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    // Обработчики событий для кнопок перехода на другие страницы
        mainPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("mainpage"));
        calculatorPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("calorieCalculator"));
        createdMenuPageBtn.setOnAction(event -> NavigationMenu.navigateToPage("createdMenu"));
        changePageBtn.setOnAction(event -> NavigationMenu.navigateToPage("settings"));
        loseWeightMenuButton.setOnAction(event -> NavigationMenu.navigateToPage("loseWeight"));
        forecastMenuItem.setOnAction(event -> NavigationMenu.navigateToPage("forecast"));

    }
}