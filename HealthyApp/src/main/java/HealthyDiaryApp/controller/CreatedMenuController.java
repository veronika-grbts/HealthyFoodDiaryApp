/*
 * CreatedMenuController  class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description:Контролер для створення меню користувача та відображення його в таблиці.
 */
package HealthyDiaryApp.controller;

import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import HealthyDiaryApp.entity.User;
import HealthyDiaryApp.entity.UserSelectedMenu;
import HealthyDiaryApp.filePDF.PDFGenerator;
import HealthyDiaryApp.menu.CreatedMenu;
import HealthyDiaryApp.singleton.ApplicationContext;
import HealthyDiaryApp.enums.Period;
import HealthyDiaryApp.util.DrinkComponent;
import HealthyDiaryApp.model.CustomMenuItem;
import HealthyDiaryApp.util.MealOptionComponent;
import HealthyDiaryApp.util.UserSelectedMenuComponent;

import java.util.List;

public class CreatedMenuController {
    private MealOptionComponent mealOptionComponent = new MealOptionComponent();
    private DrinkComponent drinkComponent = new DrinkComponent();
    private UserSelectedMenuComponent userSelectedMenuComponent = new UserSelectedMenuComponent();
    private boolean ingredientsColumnCreated = false;

    public void initialize(TableView<CustomMenuItem> tableProduct, ComboBox<String> productsComboBox, CheckBox checkBoxCreatedPdf,
                           Button updateBtn, ImageView updateImage, Button mainPageBtn, Button calculatorPageBtn,
                           Button createdMenuPageBtn, Button changePageBtn, Button loseWeightMenuButton, Button forecastMenuItem,
                           MenuItem statisticsMenuItem, Button createdMenutBtn, TableColumn<UserSelectedMenu, String> typeMealColumn,
                           TableColumn<UserSelectedMenu, String> nameProductColumn, TableColumn<UserSelectedMenu, Double> quantityColumn) {

        configureTable(tableProduct, typeMealColumn, nameProductColumn, quantityColumn);
        ObservableList<String> comboBoxItems = FXCollections.observableArrayList(
                "Один день",
                "Три дні",
                "Тиждень",
                "Два тижні"
        );
        productsComboBox.setItems(comboBoxItems);

        CreatedMenu createdMenu = new CreatedMenu();
        createdMenutBtn.setOnAction(actionEvent -> {
            String selectedPeriod = productsComboBox.getValue();
            if (selectedPeriod == null || selectedPeriod.isEmpty()) {
                ErrorDialogController.showErrorAlert("Помилка", "Будь-ласака оберіть період для створення меню.");
                return; // Прерываем выполнение дальнейших действий
            }
            int numberOfDays = getNumberOfDaysForPeriod(selectedPeriod);
            for (int i = 0; i < numberOfDays; i++) {
                createdMenu.createMenu();
            }
            populateTable(tableProduct, productsComboBox, checkBoxCreatedPdf);
            configureTable(tableProduct, typeMealColumn, nameProductColumn, quantityColumn);
            boolean hasCause = checkBoxCreatedPdf.isSelected();
            if (hasCause) {
                createPDF(tableProduct,"C:/Users/User/Desktop", "Меню згенероване програмою HealthyDiary");
            }
        });

        updateBtn.setOnAction(event -> {
            handleUpdateButtonClick(tableProduct, productsComboBox);
            rotateImage(updateImage);
        });

    }

    private void rotateImage(ImageView updateImage) {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), updateImage);
        rotateTransition.setByAngle(360);
        rotateTransition.play();
    }

    private void createPDF(TableView<CustomMenuItem> tableProduct, String filePath, String fileName) {
        ObservableList<CustomMenuItem> itemsForPDF = getItemsForPDF(tableProduct);
        PDFGenerator.createPDF(itemsForPDF, filePath, fileName);
    }

    private void configureTable(TableView<CustomMenuItem> tableProduct, TableColumn<UserSelectedMenu, String> typeMealColumn,
                                TableColumn<UserSelectedMenu, String> nameProductColumn, TableColumn<UserSelectedMenu, Double> quantityColumn) {

        typeMealColumn.setCellValueFactory(new PropertyValueFactory<>("mealType"));
        nameProductColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        tableProduct.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableProduct.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(CustomMenuItem item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty && item.isDayHeader()) {
                    setStyle("-fx-background-color: lightgrey;");
                } else {
                    setStyle("");
                }
            }
        });

    }

    private ObservableList<CustomMenuItem> getItemsForPDF(TableView<CustomMenuItem> tableProduct) {
        ObservableList<CustomMenuItem> itemsForPDF = FXCollections.observableArrayList();
        ObservableList<CustomMenuItem> items = tableProduct.getItems();
        for (CustomMenuItem item : items) {
            if (!item.isDayHeader()) {
                itemsForPDF.add(item);
            }
        }
        return itemsForPDF;
    }

    private void populateTable(TableView<CustomMenuItem> tableProduct, ComboBox<String> productsComboBox, CheckBox checkBoxCreatedPdf) {
        tableProduct.getItems().clear();
        String selectedPeriod = productsComboBox.getValue();
        int numberOfDays = getNumberOfDaysForPeriod(selectedPeriod);
        User user = getUserFromApplicationContext();

        ObservableList<CustomMenuItem> rowData = generateMenuForDay(user, numberOfDays);
        for (CustomMenuItem item : rowData) {
            if (!item.isDayHeader()) {
                tableProduct.getItems().add(item);
            }
        }
    }

    private ObservableList<CustomMenuItem> generateMenuForDay(User user, int numberOfDays) {
        ObservableList<CustomMenuItem> rowData = FXCollections.observableArrayList();

        List<UserSelectedMenu> userMenus = userSelectedMenuComponent.getAllUserMenus(user.getPhoneNumber(), numberOfDays);
        int day = 1; // Начальный день

        for (UserSelectedMenu menu : userMenus) {

            CustomMenuItem dayHeader = new CustomMenuItem("День " + day, "", user.getTotalCaloricUser());
            dayHeader.setDayHeader(true);
            rowData.add(dayHeader); // Добавляем заголовок дня в список данных для таблицы
            day++;
            String breakfastName = mealOptionComponent.getMealOptionNameById(
                    menu.getBreakfast().getIdOption());
            String snackFirstName = mealOptionComponent.getMealOptionNameById(
                    menu.getSnackDishId().getIdOption());
            String lunchName = mealOptionComponent.getMealOptionNameById(
                    menu.getLunch().getIdOption());
            String dinnerName = mealOptionComponent.getMealOptionNameById(
                    menu.getDinner().getIdOption());
            String breakfastDrinkName = drinkComponent.getDrinkNameById(
                    menu.getBreakfastDrink().getIdDrink());
            String lunchDrinkName = drinkComponent.getDrinkNameById(
                    menu.getLunchDrink().getIdDrink());
            String dinnerDrinkName = drinkComponent.getDrinkNameById(
                    menu.getDinnerDrink().getIdDrink());
            String snackSecondName = mealOptionComponent.getMealOptionNameById(
                    menu.getSnackSecondDishId().getIdOption());

            rowData.add(new CustomMenuItem("Сніданок",
                    breakfastName, menu.getGramsForBreakfastSelectedMenu()));

            rowData.add(new CustomMenuItem("Напій для сніданку",
                    breakfastDrinkName, 200.0));

            rowData.add(new CustomMenuItem("Перший Перекус",
                    snackFirstName, menu.getGramsForSnackFirstDishGrams()));

            rowData.add(new CustomMenuItem("Обід",
                    lunchName, menu.getGramsForLunchSelectedMenu()));

            if (menu.getAdditionalDishId() != null) {
                String additionalDishName = mealOptionComponent.getMealOptionNameById(
                        menu.getAdditionalDishId().getIdOption());

                double additionalDishQuantity = menu.getLunchAdditionalDishGrams();
                rowData.add(new CustomMenuItem("Друга страва для обіду ",
                        additionalDishName, additionalDishQuantity));
            }
            rowData.add(new CustomMenuItem("Напій для обіду", lunchDrinkName, 200.0));
            rowData.add(new CustomMenuItem("Другий Перекус",
                    snackSecondName, menu.getGramsForSnackSecondDishGrams()));

            rowData.add(new CustomMenuItem("Вечеря", dinnerName,
                    menu.getGramsForDinnerSelectedMenu()));
            if (menu.getAdditionalDinnerDishId() != null) {
                String dinnerAdditionalDishName = mealOptionComponent.getMealOptionNameById(
                        menu.getAdditionalDinnerDishId().getIdOption()
                );

                double dinnerAdditionalDishQuantity = menu.getDinnerAdditionalDishGrams();
                rowData.add(new CustomMenuItem("Друга страва для вечері",
                        dinnerAdditionalDishName, dinnerAdditionalDishQuantity));
            }
            rowData.add(new CustomMenuItem("Напій для вічері", dinnerDrinkName, 200.0));
        }
        return rowData;
    }

    public class SpanningTableCell<S> extends TableCell<S, String> {
        private final Label label;

        public SpanningTableCell() {
            this.label = new Label();
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(label);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                label.setText(item);
            }
        }
    }

    private User getUserFromApplicationContext() {
        return ApplicationContext.getInstance().getCurrentUser();
    }

    private int getNumberOfDaysForPeriod(String selectedPeriod) {
        for (Period period : Period.values()) {
            if (period.getLabel().equals(selectedPeriod)) {
                return period.getDays();
            }
        }
        return 1; // По умолчанию
    }

    private void handleUpdateButtonClick(TableView<CustomMenuItem> tableProduct, ComboBox<String> productsComboBox) {
        // Очистка таблицы
        tableProduct.getItems().clear();
        User user = getUserFromApplicationContext();
        String selectedPeriod = productsComboBox.getValue();
        int numberOfDays = getNumberOfDaysForPeriod(selectedPeriod);
        userSelectedMenuComponent.deleteUserMenus(user.getPhoneNumber(),numberOfDays);
    }
}
