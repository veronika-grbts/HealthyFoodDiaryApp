import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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
        calculatorPageBtn.setOnAction(event -> {
            try {
                HibbernateRunner.setRoot("calorieCalculator");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        createdMenuPageBtn.setOnAction(event -> {
            try {
                HibbernateRunner.setRoot("createdMenu");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }
}


