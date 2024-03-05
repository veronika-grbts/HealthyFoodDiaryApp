package project.view;

import java.net.URL;
import java.util.*;


import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import project.controller.SignUpController;
import project.entity.ActivityLevel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SignUpView implements Initializable {
    private SignUpController signUpController = new SignUpController();
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane yourImageView;

    @FXML
    private AnchorPane imagePlus;

    @FXML
    private AnchorPane signUpPane1;

    @FXML
    private TextField signUpName;

    @FXML
    private TextField sighUpPhone;

    @FXML
    private Button nextBtn1;

    @FXML
    private AnchorPane signUpPane2;

    @FXML
    private TextField signUpAge;

    @FXML
    private TextField signUpWeight;

    @FXML
    private TextField signUpHeight;

    @FXML
    private ComboBox<ActivityLevel> signUpActivityLevel;

    @FXML
    private ComboBox<String> signUpGender;

    @FXML
    private Button nextBtn2;

    @FXML
    private AnchorPane signUpPane3;

    @FXML
    private CheckBox checkBoxAllergy;

    @FXML
    private CheckBox checkBoxCause;

    @FXML
    private Button finishBtn;

    @FXML
    private ImageView ImagePlus;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signUpActivityLevel.getItems().addAll(ActivityLevel.High,
                ActivityLevel.Medium, ActivityLevel.Low);

        signUpGender.getItems().addAll("чоловік", "жінка");

        nextBtn1.setOnAction(event -> {
            signUpPane1.setVisible(false);
            signUpPane2.setVisible(true);
        });

        nextBtn2.setOnAction(this::handleImagePlusClicked);

        finishBtn.setOnAction(event -> {
            handleFinishBtn(event);
        });
    }

    @FXML
    void handleNextBtn1(ActionEvent event) {
        signUpController.handleNextBtn1(nextBtn1, signUpPane1, signUpPane2);
    }

    @FXML
    void handleNextBtn2(ActionEvent event) {
        signUpController.handleNextBtn2(nextBtn2, signUpPane2, signUpPane3, imagePlus, sighUpPhone);
    }

    @FXML
    void handleFinishBtn(ActionEvent event) {
        signUpController.handleFinishBtn(finishBtn, signUpName, sighUpPhone, signUpAge, signUpWeight, signUpHeight,
                signUpGender, signUpActivityLevel, checkBoxAllergy, checkBoxCause, signUpPane3);
    }

    @FXML
    void handleImagePlusClicked(MouseEvent event) {
        signUpController.handleImagePlusClicked(sighUpPhone, signUpPane3);
    }

    private void handleImagePlusClicked(ActionEvent event) {
        signUpPane2.setVisible(false);
        signUpPane3.setVisible(true);
        handleImagePlusClicked(event);
    }
}
