package HealthyDiaryApp.view;

import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DraggableWindow {
    private double xOffset = 0;
    private double yOffset = 0;

    public void makeDraggable(Stage stage, Parent root) {
        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
}
