package HealthyDiaryApp.view;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ErrorDialogView {
    private double xOffset = 0;
    private double yOffset = 0;


    @FXML
    private Label messageLabel;


    @FXML
    private ImageView backImg;


    @FXML
    private Label titleMessager;

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void setTitleMessager(String title){
        titleMessager.setText(title);
    }

    @FXML
    private void handleImageClick() {
        // Получаем сцену (окно) изображения
        Scene scene = backImg.getScene();
        // Закрываем сцену (окно)
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }

    @FXML
    private void handleMousePressed(MouseEvent event) {
        // Сохраняем начальные координаты мыши
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    private void handleMouseDragged(MouseEvent event) {
        // Получаем текущие координаты мыши
        double x = event.getScreenX() - xOffset;
        double y = event.getScreenY() - yOffset;

        // Перемещаем окно на новые координаты
        backImg.getScene().getWindow().setX(x);
        backImg.getScene().getWindow().setY(y);
    }

}
