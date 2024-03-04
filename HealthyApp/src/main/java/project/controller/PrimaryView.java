package project.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class PrimaryView{
    @FXML
    private TextField loginField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private Button signInBtn;

    @FXML
    private Button signUpBtn;

    public PrimaryView() {

    }

    public TextField getLoginField() {
        return loginField;
    }

    public TextField getPhoneNumberField() {
        return phoneNumberField;
    }

    public Button getSignInBtn() {
        return signInBtn;
    }

    public Button getSignUpBtn() {
        return signUpBtn;
    }
}
