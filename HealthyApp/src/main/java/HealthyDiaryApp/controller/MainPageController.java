package HealthyDiaryApp.controller;

import javafx.scene.control.TextField;
import HealthyDiaryApp.entity.User;
import HealthyDiaryApp.singleton.ApplicationContext;

public class MainPageController {
    public void fillUserData(TextField name_id, TextField numberPhone,
                             TextField age,TextField height, TextField weight,
                             TextField gender, TextField allergy){
        User user = getUserFromApplicationContext();
        name_id.setText(user.getNameUser());
        numberPhone.setText(String.valueOf(user.getPhoneNumber()));
        age.setText(String.valueOf(user.getAgeUser()));
        height.setText(String.valueOf(user.getHeightUser()) +" см");
        weight.setText(String.valueOf(user.getWeightUser()) + " кг");
        gender.setText(user.isGenderUser() ? "чоловік" : "жінка");
        allergy.setText(user.isAllergiesUser() ? "так" : "ні");

    }

    private User getUserFromApplicationContext() {
        return ApplicationContext.getInstance().getCurrentUser();
    }

}
