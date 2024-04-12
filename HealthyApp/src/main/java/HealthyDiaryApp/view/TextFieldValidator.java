package HealthyDiaryApp.view;

import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class TextFieldValidator {

    public static boolean isPhoneValid(String phone) {
        return phone.matches("^380\\d{9}$");
    }

    public static boolean isAgeValid(String age) {
        return age.matches("\\d+");
    }

    public static boolean isHeightValid(String height) {
        return height.matches("\\d+\\.?\\d*");
    }

    public static boolean isWeightValid(String weight) {
        return weight.matches("\\d+(\\.\\d+)?");
    }

    public static boolean isCalocicValid(String caloric) {
        return caloric.matches("\\d+(\\.\\d+)?");
    }
    public static boolean isValidField(TextField field) {
        if (field == null || field.getText().trim().isEmpty()) {
            setInvalidStyle(field);
            return false;
        }

        boolean isValid = false;

        switch (field.getId()) {
            case "sighUpPhone":
                isValid = isPhoneValid(field.getText());
                break;
            case "signUpAge":
                isValid = isAgeValid(field.getText());
                break;
            case "signUpWeight":
                isValid = isWeightValid(field.getText());
                break;
            case "signUpHeight":
                isValid = isHeightValid(field.getText());
                break;
            case "calorieProductField":
                isValid = isCalocicValid(field.getText());
                break;
            case  "fatproductField":
                isValid = isCalocicValid(field.getText());
                break;
            case  "proteinProductField":
                isValid = isCalocicValid(field.getText());
                break;
            case  "carbsProductsField":
                isValid = isCalocicValid(field.getText());
                break;
            case "nameProductField":
                isValid = !field.getText().isEmpty();
                break;
            default:
                isValid = true;
                break;
        }

        if (isValid) {
            setValidStyle(field);
        } else {
            setInvalidStyle(field);
        }

        return isValid;
    }

    static void setInvalidStyle(TextField field) {
        field.setStyle("-fx-border-color: red;");
    }

    static void setValidStyle(TextField field) {
        field.setStyle("");
    }
}
