import java.net.URL;
import java.util.ResourceBundle;

import entity.UserSelectedProduct;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CreatedMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView updateImage;

    @FXML
    private Button updateBtn;

    @FXML
    private Button mainPageBtn;

    @FXML
    private Button calculatorPageBtn;

    @FXML
    private Button createdMenuPageBtn;

    @FXML
    private Button changePageBtn;

    @FXML
    private ComboBox<?> productsComboBox;

    @FXML
    private Button createdMenutBtn;

    @FXML
    private TableView<?> tableProduct;

    @FXML
    private TableColumn<?, ?> state;

    @FXML
    private TableColumn<?, ?> nameProduct;

    @FXML
    private TableColumn<?, ?> quantity;

    @FXML
    private AnchorPane PaneAddProduct;

    @FXML
    private TextField nameProductField;

    @FXML
    private TextField calorieProductField;

    @FXML
    private TextField proteinProductField;

    @FXML
    private TextField fatproductField;

    @FXML
    private TextField carbsProductsField;

    @FXML
    private Button addNewProductBtn;

    @FXML
    private ImageView closePane;

    @FXML
    void ClosePane(MouseEvent event) {

    }

    @FXML
    void initialize() {
        configureTable();
        CreatedMenu createdMenu = new CreatedMenu();
        createdMenutBtn.setOnAction(actionEvent -> {
            createdMenu.createMenu();
        });
    }

    private void configureTable() {
        // Настройка столбцов таблицы
        nameProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Добавление стилей из CSS
        tableProduct.getStyleClass().add("table-view");
        nameProduct.getStyleClass().add("table-column");
        quantity.getStyleClass().add("table-column");
    }
}


