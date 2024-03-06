package project.view;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import project.controller.LoseWeightController;
import project.navigation.BaseMenuClass;

public class LoseWeightView extends BaseMenuClass {
    private LoseWeightController loseWeightController = new LoseWeightController();
    private static final  int AXIS_OFFSET_Y = 100;
    private static final int ANIMATION_DURATION = 500;

    @FXML
    private Button updateBestWeightUserBtn;

    @FXML
    private VBox VBoxMenu;

    @FXML
    private Button mainPageBtn;

    @FXML
    private Button calculatorPageBtn;

    @FXML
    private Button createdMenuPageBtn;

    @FXML
    private SplitMenuButton loseWeightMenuButton;

    @FXML
    private MenuItem forecastMenuItem;

    @FXML
    private Button changePageBtn;

    @FXML
    private TextField bodyMassIndexFieldText;

    @FXML
    private Label descriptionBodyMassIndexLable;

    @FXML
    private Label nameBodyMassIndexLable;

    @FXML
    private TextField currentCaloricIntakeTextField;

    @FXML
    private TextField bestweightFieldText;

    @FXML
    private PieChart diagramAboutWeightLossStage;

    @FXML
    private AnchorPane oopsWindowPane;

    @FXML
    private Button updateCouseInPageBtn;

    @FXML
    private AnchorPane updateBestWeightPage;

    @FXML
    private ImageView closeWindowImg;

    @FXML
    private TextField newBestWeighttextField;

    @FXML
    private Button updateBestWeightBtn;

    @FXML
    private AnchorPane updateCouseUserPage;

    @FXML
    private ImageView closeWindowUpdateCouseImg;

    @FXML
    private Button updateCouseuserBtn;

    @FXML
    private AnchorPane userIdealWeightPage;

    @FXML
    private ImageView closeWindowLowWeightImg;


    @FXML
    private ImageView closeWindowIdealWeightImg;

    @FXML
    private AnchorPane userLowWeightPage;

    @FXML
    void initialize() {

        initializeButtons(mainPageBtn, calculatorPageBtn, createdMenuPageBtn, changePageBtn);
        initializeMenuButton(loseWeightMenuButton);
        initializeMenuItem(forecastMenuItem);
        AnimationButton.addHoverAnimation(updateBestWeightUserBtn);
        AnimationButton.addFadeAnimation(updateCouseuserBtn);
        AnimationButton.addHoverAnimation(updateBestWeightBtn);

        loseWeightController.initializeUserInfo( bodyMassIndexFieldText,
                 nameBodyMassIndexLable,  descriptionBodyMassIndexLable,
                 userLowWeightPage, userIdealWeightPage,
                 bestweightFieldText,  currentCaloricIntakeTextField,
                 oopsWindowPane,  updateCouseInPageBtn,
                 updateCouseUserPage,  diagramAboutWeightLossStage);
        closeWindowIdealWeightImg.setOnMouseClicked(this::ClosePaneWithIdealWeight);

        updateCouseInPageBtn.setOnAction(event -> {
            updateCouseUserPage.setVisible(true);
        });

        updateBestWeightBtn.setOnAction(event -> {
            loseWeightController.changeTargetWeight(newBestWeighttextField,  updateBestWeightPage,  bestweightFieldText,  diagramAboutWeightLossStage);
        });

        updateCouseuserBtn.setOnAction(event -> {
            loseWeightController.initializeUpdateCourseUser(oopsWindowPane, updateCouseUserPage,
                     bestweightFieldText, currentCaloricIntakeTextField,
                     diagramAboutWeightLossStage, userIdealWeightPage,updateBestWeightPage);
        });
        updateBestWeightUserBtn.setOnAction(event -> {
            loseWeightController.initializeUpdateBestWeight(userLowWeightPage,
                    userIdealWeightPage, updateBestWeightPage);

            closeWindowImg.setOnMouseClicked(this::ClosePane);
            closeWindowLowWeightImg.setOnMouseClicked(this::ClosePaneWithLowWeight);
            closeWindowIdealWeightImg.setOnMouseClicked(this::ClosePaneWithIdealWeight);
        });


    }

    @FXML
    void ClosePane(MouseEvent event) {
        updateBestWeightPage.setVisible(false);
    }

    @FXML
    void ClosePaneWithIdealWeight(MouseEvent event) {
        userIdealWeightPage.setVisible(false);
    }

    @FXML
    void ClosePaneWithLowWeight(MouseEvent event) {
        userLowWeightPage.setVisible(false);
    }
}