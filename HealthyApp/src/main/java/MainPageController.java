import java.net.URL;
import java.util.ResourceBundle;

import entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import util.HibernateMethods;

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
        name_id.setText(user.getName());
        numberPhone.setText(String.valueOf(user.getPhoneNumber()));
        age.setText(String.valueOf(user.getAge()));
        height.setText(String.valueOf(user.getHeight()) +" см");
        weight.setText(String.valueOf(user.getWeight()) + " кг");
        gender.setText(user.isGender() ? "чоловік" : "жінка");
        allergy.setText(user.isAllergies() ? "так" : "ні");
    }
}
