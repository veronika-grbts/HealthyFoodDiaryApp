package project.controller;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import project.singleton.ApplicationContext;
import project.method.CreatedMenu;
import project.entity.User;
import project.entity.UserSelectedMenu;
import project.tableView.CustomMenuItem;
import project.tableView.Period;
import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.util.Duration;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import project.util.HibernateMethods;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;


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
        private ComboBox<String> productsComboBox;

        @FXML
        private Button createdMenutBtn;

        @FXML
        private CheckBox checkBoxCreatedPdf;

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
        // Установка фабрики ячеек для столбца typeMealColumn
     //   typeMealColumn.setCellFactory(column -> new SpanningTableCell<>());
        configureTable();
        // Отримуємо список варіантів для ComboBox
        ObservableList<String> comboBoxItems = FXCollections.observableArrayList("Один день", "Три дні", "Тиждень", "Два тижні");
        productsComboBox.setItems(comboBoxItems);

        CreatedMenu createdMenu = new CreatedMenu();
        createdMenutBtn.setOnAction(actionEvent -> {
            // Получаем выбранное количество дней из комбобокса
            String selectedPeriod = productsComboBox.getValue();
            int numberOfDays = getNumberOfDaysForPeriod(selectedPeriod);

            // Создаем новое меню для каждого дня и добавляем его в таблицу
            for (int i = 0; i < numberOfDays; i++) {
                createdMenu.createMenu();
            }

            // После создания всех меню заполняем таблицу и создаем PDF
            populateTable();
            configureTable();
            boolean hasCause = checkBoxCreatedPdf.isSelected();
            if (hasCause) {
                createPDF("C:/Users/User/Desktop", "Меню згенероване програмою HealthyDiary");
            }
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
        updateBtn.setOnAction(event -> {
            handleUpdateButtonClick(event);
            rotateImage();
        });
    }
    private void rotateImage() {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), updateImage);
        rotateTransition.setByAngle(360);
        rotateTransition.play();
    }

    private void createPDF(String filePath, String fileName) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(fileName);
            fileChooser.setInitialDirectory(new File(filePath));

            // Устанавливаем фильтр для файлов PDF
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);

            // Получаем основное окно приложения
            Stage stage = (Stage) mainPageBtn.getScene().getWindow();

            // Показываем диалоговое окно сохранения файла
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                PDFont font = PDType0Font.load(document, new FileInputStream(new File("C:/Windows/Fonts/ARIALUNI.TTF")));

                // Задание размера шрифта и выбор шрифта Arial Unicode MS
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);

                // Записываем данные из списка без заголовков дней в PDF
                ObservableList<CustomMenuItem> itemsForPDF = getItemsForPDF();
                contentStream.showText("Сгенероване меню програмою HealthyDiary");
                contentStream.newLineAtOffset(0, -20); // Сдвигаем на следующую строку

                int day = 1;
                boolean isFirstMenuOfDay = true;
                int currentLine = 0; // Счетчик строк на странице
                for (CustomMenuItem item : itemsForPDF) {
                    // Проверяем, достигли ли мы предела строк на странице
                    if (currentLine >= 32) {
                        // Добавляем новую страницу
                        contentStream.endText();
                        contentStream.close();
                        page = new PDPage();
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        contentStream.setFont(font, 12);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(100, 700);
                        currentLine = 0; // Сбрасываем счетчик строк
                    }

                    // Если это первое меню для нового дня, выводим номер дня
                    if (isFirstMenuOfDay) {
                        contentStream.showText("День " + day + ":");
                        contentStream.newLineAtOffset(0, -20); // Сдвигаем на следующую строку
                        currentLine++; // Увеличиваем счетчик строк
                        isFirstMenuOfDay = false;
                    }

                    // Устанавливаем цвет и стиль для вывода типа еды
                    contentStream.setNonStrokingColor(Color.BLUE); // устанавливаем цвет текста
                    contentStream.setFont(font, 12); // устанавливаем шрифт и размер
                    contentStream.showText("Прийом їжі: ");
                    contentStream.showText(item.getMealType());
                    contentStream.newLineAtOffset(0, -20); // Сдвигаем на следующую строку
                    currentLine++; // Увеличиваем счетчик строк

                    // Устанавливаем цвет и стиль для вывода названия
                    contentStream.setNonStrokingColor(Color.BLACK); // устанавливаем цвет текста
                    contentStream.setFont(font, 12); // устанавливаем шрифт и размер
                    contentStream.showText("Назва: ");
                    contentStream.showText(item.getName());
                    contentStream.newLineAtOffset(0, -20); // Сдвигаем на следующую строку
                    currentLine++; // Увеличиваем счетчик строк

                    // Устанавливаем цвет и стиль для вывода количества
                    contentStream.setNonStrokingColor(Color.BLACK); // устанавливаем цвет текста
                    contentStream.setFont(font, 12); // устанавливаем шрифт и размер
                    contentStream.showText("Кількість: ");
                    contentStream.showText(String.valueOf(item.getQuantity() + " г"));
                    contentStream.newLineAtOffset(0, -20); // Сдвигаем на следующую строку
                    currentLine++; // Увеличиваем счетчик строк

                    // Если это последнее меню для текущего дня, увеличиваем номер дня
                    if (item.isLastMenuOfDay()) {
                        day++;
                        isFirstMenuOfDay = true; // Сбрасываем флаг для следующего дня
                    }
                }
                contentStream.endText();
                contentStream.close();


                // Сохраняем PDF
                document.save(file);
                document.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void configureTable() {
        // Устанавливаем фабрику для значений столбцов
        typeMealColumn.setCellValueFactory(new PropertyValueFactory<>("mealType"));
        nameProductColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Устанавливаем политику изменения размера столбцов
        tableProduct.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Добавляем стиль для столбца с ингредиентами
        if (!ingredientsColumnCreated) {
            TableColumn<CustomMenuItem, Void> ingredientsColumn = new TableColumn<>("Інгрідієнти");
            ingredientsColumn.setCellFactory(param -> new TableCell<CustomMenuItem, Void>() {
                private final Button btn = new Button("Інгрідієнти");

                {
                    btn.setOnAction(event -> {
                        CustomMenuItem item = getTableView().getItems().get(getIndex());
                        if (item != null) {
                            // Обработка нажатия кнопки
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || getTableRow().getItem() == null || ((CustomMenuItem) getTableRow().getItem()).isDayHeader()) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        setGraphic(btn);
                        setText(null);
                    }
                }
            });
            tableProduct.getColumns().add(ingredientsColumn);
            ingredientsColumnCreated = true;
        }

        // Добавляем стиль для строки-заголовка дня
        tableProduct.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(CustomMenuItem item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty && item.isDayHeader()) {
                    setStyle("-fx-background-color: lightgrey;");
                } else {
                    setStyle(""); // Сбрасываем стиль для обычных строк
                }
            }
        });

        }

    private ObservableList<CustomMenuItem> getItemsForPDF() {
        ObservableList<CustomMenuItem> itemsForPDF = FXCollections.observableArrayList();
        ObservableList<CustomMenuItem> items = tableProduct.getItems();
        for (CustomMenuItem item : items) {
            if (!item.isDayHeader()) {
                itemsForPDF.add(item);
            }
        }
        return itemsForPDF;
    }

    private void populateTable() {
        tableProduct.getItems().clear();
        String selectedPeriod = productsComboBox.getValue();
        int numberOfDays = getNumberOfDaysForPeriod(selectedPeriod);
        User user = getUserFromApplicationContext();
        // Вызываем generateMenuForDay только один раз с учетом количества дней
        ObservableList<CustomMenuItem> rowData = generateMenuForDay(user, numberOfDays);

        // Добавляем элементы в таблицу, пропуская кнопку "Ингредиенты" для заголовков дней
        for (CustomMenuItem item : rowData) {
            if (!item.isDayHeader()) {
                tableProduct.getItems().add(item);
            }
        }
    }

    private ObservableList<CustomMenuItem> generateMenuForDay(User user, int numberOfDays) {
        ObservableList<CustomMenuItem> rowData = FXCollections.observableArrayList();

        List<UserSelectedMenu> userMenus = hibernateMethods.getAllUserMenus(user.getPhoneNumber(), numberOfDays);
        int day = 1; // Начальный день

        for (UserSelectedMenu menu : userMenus) {
            // Добавляем строку с информацией о дне и его номере
            CustomMenuItem dayHeader = new CustomMenuItem("День " + day, "", user.getTotalCaloricUser());
            dayHeader.setDayHeader(true);
            rowData.add(dayHeader); // Добавляем заголовок дня в список данных для таблицы
            day++; // Увеличиваем номер дня
            String breakfastName = hibernateMethods.getMealOptionNameById(menu.getBreakfast().getIdOption());
            String snackFirstName = hibernateMethods.getMealOptionNameById(menu.getSnackDishId().getIdOption());
            String lunchName = hibernateMethods.getMealOptionNameById(menu.getLunch().getIdOption());
            String dinnerName = hibernateMethods.getMealOptionNameById(menu.getDinner().getIdOption());
            String breakfastDrinkName = hibernateMethods.getDrinkNameById(menu.getBreakfastDrink().getIdDrink());
            String lunchDrinkName = hibernateMethods.getDrinkNameById(menu.getLunchDrink().getIdDrink());
            String dinnerDrinkName = hibernateMethods.getDrinkNameById(menu.getDinnerDrink().getIdDrink());
            String snackSecondName = hibernateMethods.getMealOptionNameById(menu.getSnackSecondDishId().getIdOption());

            rowData.add(new CustomMenuItem("Сніданок", breakfastName, menu.getGramsForBreakfastSelectedMenu()));
            rowData.add(new CustomMenuItem("Напій для сніданку", breakfastDrinkName, 200.0));
            rowData.add(new CustomMenuItem("Перший Перекус", snackFirstName, menu.getGramsForSnackFirstDishGrams()));
            rowData.add(new CustomMenuItem("Обід", lunchName, menu.getGramsForLunchSelectedMenu()));
            if (menu.getAdditionalDishId() != null) {
                String additionalDishName = hibernateMethods.getMealOptionNameById(menu.getAdditionalDishId().getIdOption());
                double additionalDishQuantity = menu.getLunchAdditionalDishGrams();
                rowData.add(new CustomMenuItem("Друга страва для обіду ", additionalDishName, additionalDishQuantity));
            }
            rowData.add(new CustomMenuItem("Напій для обіду", lunchDrinkName, 200.0));
            rowData.add(new CustomMenuItem("Другий Перекус", snackSecondName, menu.getGramsForSnackSecondDishGrams()));
            rowData.add(new CustomMenuItem("Вечеря", dinnerName, menu.getGramsForDinnerSelectedMenu()));
            if (menu.getAdditionalDinnerDishId() != null) {
                String dinnerAdditionalDishName = hibernateMethods.getMealOptionNameById(menu.getAdditionalDinnerDishId().getIdOption());
                double dinnerAdditionalDishQuantity = menu.getDinnerAdditionalDishGrams();
                rowData.add(new CustomMenuItem("Друга страва для вечері", dinnerAdditionalDishName, dinnerAdditionalDishQuantity));
            }
            rowData.add(new CustomMenuItem("Напій для вічері", dinnerDrinkName, 200.0));


        }
        return rowData;
    }




    // Создаем специальную ячейку для объединения
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

    private void handleUpdateButtonClick(ActionEvent event) {
        // Очистка таблицы
        tableProduct.getItems().clear();
        User user = getUserFromApplicationContext();
        // Удаление вариантов меню из базы данных
        String selectedPeriod = productsComboBox.getValue();
        int numberOfDays = getNumberOfDaysForPeriod(selectedPeriod);
        hibernateMethods.deleteUserMenus(user.getPhoneNumber(),numberOfDays);
    }
}