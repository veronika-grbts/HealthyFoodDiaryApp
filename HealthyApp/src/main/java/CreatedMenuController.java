import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entity.User;
import entity.UserSelectedMenu;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import util.HibernateMethods;

public class CreatedMenuController {
    private HibernateMethods hibernateMethods = new HibernateMethods();
    private boolean ingredientsColumnCreated = false;


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
        private TableView<CustomMenuItem> tableProduct;

        @FXML
        private TableColumn<UserSelectedMenu, String> typeMealColumn;

        @FXML
        private TableColumn<UserSelectedMenu, String> nameProductColumn;

        @FXML
        private TableColumn<UserSelectedMenu, Double> quantityColumn;

    @FXML
    void initialize() {
        configureTable();
        CreatedMenu createdMenu = new CreatedMenu();
        createdMenutBtn.setOnAction(actionEvent -> {
            createdMenu.createMenu();
            populateTable();
        });

        calculatorPageBtn.setOnAction(event -> {
            try {
                HibbernateRunner.setRoot("calorieCalculator");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        mainPageBtn.setOnAction(event -> {
            try {
                HibbernateRunner.setRoot("mainpage");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void configureTable() {

        typeMealColumn.setCellValueFactory(new PropertyValueFactory<>("mealType"));
        nameProductColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));


        tableProduct.getStyleClass().add("table-view");
        nameProductColumn.getStyleClass().add("table-column");
        quantityColumn.getStyleClass().add("table-column");


        if (!ingredientsColumnCreated) {
            TableColumn<CustomMenuItem, Void> ingredientsColumn = new TableColumn<>("Інгрідієнти");
            ingredientsColumn.setCellFactory(param -> {
                final TableCell<CustomMenuItem, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Інгрідієнти");{
                        btn.setOnAction(event -> {
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                cell.setStyle("-fx-alignment: CENTER;");
                return cell;
            });

            tableProduct.getColumns().add(ingredientsColumn);

            ingredientsColumnCreated = true;
        }
    }

    private void populateTable() {
        tableProduct.getItems().clear();
        User user = getUserFromApplicationContext();
        UserSelectedMenu lastMenu = hibernateMethods.getLastUserMenu(user.getPhoneNumber());

        if (lastMenu != null) {
            String breakfastName = hibernateMethods.getMealOptionNameById(lastMenu.getBreakfast().getIdOption());
            String lunchName = hibernateMethods.getMealOptionNameById(lastMenu.getLunch().getIdOption());
            String dinnerName = hibernateMethods.getMealOptionNameById(lastMenu.getDinner().getIdOption());


            String breakfastDrinkName = hibernateMethods.getDrinkNameById(lastMenu.getBreakfastDrink().getIdDrink());
            String lunchDrinkName = hibernateMethods.getDrinkNameById(lastMenu.getLunchDrink().getIdDrink());
            String dinnerDrinkName = hibernateMethods.getDrinkNameById(lastMenu.getDinnerDrink().getIdDrink());

            //Додавання даних до таблиці
            ObservableList<CustomMenuItem> menuData = FXCollections.observableArrayList();
            menuData.add(new CustomMenuItem("Сніданок", breakfastName, lastMenu.getGramsForBreakfastSelectedMenu()));
            menuData.add(new CustomMenuItem("Сніданок", breakfastDrinkName, 200.0));
            menuData.add(new CustomMenuItem("Обід", lunchName, lastMenu.getGramsForLunchSelectedMenu()));
            menuData.add(new CustomMenuItem("Обід", lunchDrinkName, 200.0));
            menuData.add(new CustomMenuItem("Вечеря", dinnerName, lastMenu.getGramsForDinnerSelectedMenu()));
            menuData.add(new CustomMenuItem("Вечеря", dinnerDrinkName, 200.0));
            tableProduct.setItems(menuData);
        }
    }

    private User getUserFromApplicationContext() {
        return ApplicationContext.getInstance().getCurrentUser();
    }
}