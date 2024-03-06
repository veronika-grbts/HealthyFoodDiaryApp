package project.view;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import project.controller.PrimaryController;

public class PrimaryView implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button signUpBtn;

    @FXML
    private TextField loginField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private Button signInBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AnimationButton.addFadeAnimation(signUpBtn);
        AnimationButton.addHoverAnimation(signInBtn);
        signInBtn.setOnAction(event -> {
            if (loginField.getText().isEmpty() && phoneNumberField.getText().isEmpty()) {
                AnimationButton.moveButtonToLeftAndBack(signInBtn);
            }
            PrimaryController.handleSignIn(signInBtn, phoneNumberField);
        });

        signUpBtn.setOnAction(event -> {
            PrimaryController.handleSignUp(signUpBtn);
        });
    }
}