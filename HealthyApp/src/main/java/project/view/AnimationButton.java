package project.view;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class AnimationButton {
    public static void addFadeAnimation(Button button) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), button);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.5);

        button.setOnMouseEntered(event -> {
            fadeTransition.playFromStart();
        });

        button.setOnMouseExited(event -> {
            fadeTransition.stop();
            button.setOpacity(1.0);
        });
    }

    public static void addHoverAnimation(Button button) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);

        button.setOnMouseEntered(event -> {
            scaleTransition.playFromStart();
        });

        button.setOnMouseExited(event -> {
            scaleTransition.stop();
            button.setScaleX(1);
            button.setScaleY(1);
        });

        button.setOnMousePressed(event -> {
            scaleTransition.stop();
            button.setScaleX(1);
            button.setScaleY(1);
            button.setScaleX(0.9);
            button.setScaleY(0.9);
        });

        button.setOnMouseReleased(event -> {
            scaleTransition.playFromStart();
        });
    }

    public static void moveButtonToLeftAndBack(Button button) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(100), button);
        translateTransition.setByX(-20); // Смещение кнопки на 20 пикселей влево

        translateTransition.setAutoReverse(true);
        translateTransition.setCycleCount(2);

        translateTransition.play();
    }

    public static void addFadeAnimation(SplitMenuButton button) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), button);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.5);

        button.setOnMouseEntered(event -> {
            fadeTransition.playFromStart();
        });

        button.setOnMouseExited(event -> {
            fadeTransition.stop();
            button.setOpacity(1.0);
        });
    }

    public static void addHoverAnimation(ImageView imageView) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), imageView);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.5);

        imageView.setOnMouseEntered(event -> {
            fadeTransition.playFromStart();
        });

        imageView.setOnMouseExited(event -> {
            fadeTransition.stop();
            imageView.setOpacity(1.0);
        });
    }

}
