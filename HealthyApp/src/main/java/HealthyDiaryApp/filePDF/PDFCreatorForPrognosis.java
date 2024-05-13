package HealthyDiaryApp.filePDF;
import HealthyDiaryApp.calculator.LoseWeightCalculator;
import HealthyDiaryApp.controller.PrognosisController;
import HealthyDiaryApp.entity.User;
import HealthyDiaryApp.util.WeightLossGoalsComponent;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.text.DocumentException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;

import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jfree.fx.FXGraphics2D;

public class PDFCreatorForPrognosis {
    private static final String FONT_FILE_PATH = getFontFilePath();
    private WeightLossGoalsComponent weightLossGoalsComponent = new WeightLossGoalsComponent();
    private LoseWeightCalculator loseWeightCalculator = new LoseWeightCalculator();
    private static final int FONT_SIZE = 10;
    private  static String filePath = "C:/Users/User/Desktop";

    public void createPdfFileAboutPrognosis(User user) {
        String fileName = user.getNameUser() + "_возможное_похудение.pdf";
        String phoneNumber = String.valueOf(user.getPhoneNumber());
        double currentWeight = user.getWeightUser();
        double height = user.getHeightUser();
        double optimalWeight = weightLossGoalsComponent.getTargetWeightByPhoneNumber(user.getPhoneNumber());
        double currentBMI = loseWeightCalculator.calculateBMI(user.getWeightUser(), user.getHeightUser());
        try {
            String fontFilePath = getFontFilePath(); // Получаем путь к файлу шрифта
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Используем шрифт из файла
            PdfFont font = PdfFontFactory.createFont(FONT_FILE_PATH, PdfEncodings.IDENTITY_H);

            // Устанавливаем шрифт для документа
            document.setFont(font);
            // Устанавливаем шрифт и размер для документа
            document.setFontSize(FONT_SIZE);

            // Добавляем информацию в документ
            Paragraph titleFile = new Paragraph("Прогнозування схуднення ")
                    .setFontColor(ColorConstants.BLACK)
                    .setFontSize(FONT_SIZE)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBackgroundColor(ColorConstants.ORANGE);
            document.add(titleFile);

            // Добавляем информацию о пользователе
            addUserInfoToDocument(document, user, currentBMI, optimalWeight);

            // Добавляем заголовок перед таблицей
            Paragraph title = new Paragraph("Таблиця ІМТ для користувача під час схуднення")
                    .setFontColor(ColorConstants.BLACK)
                    .setFontSize(FONT_SIZE)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBackgroundColor(ColorConstants.ORANGE);
            document.add(title);

            // Добавляем таблицу с информацией об уровнях ИМТ и их диапазонах
            addBMITableToDocument(document, currentWeight, optimalWeight, height); // Передаем путь к файлу шрифта

            // Добавляем заголовок перед графиком
            Paragraph titleGraphic = new Paragraph("Прогназування ІМТ")
                    .setFontColor(ColorConstants.BLACK)
                    .setFontSize(FONT_SIZE)
                    .setBold()
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY);
            document.add(titleGraphic);

            // Создаем график JavaFX
            LineChart<Number, Number> bmiChart = createBMIChart(currentWeight, optimalWeight, user);

            // Создаем временное изображение графика
            WritableImage chartImage = bmiChart.snapshot(new SnapshotParameters(), null);

            // Преобразуем изображение в массив байтов PNG
            ByteArrayOutputStream chartOut = new ByteArrayOutputStream();
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(chartImage, null), "png", chartOut);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Добавляем изображение в PDF-документ
            Image chartPdfImage = new Image(ImageDataFactory.create(chartOut.toByteArray()));
            document.add(chartPdfImage);

            // Закрываем документ
            document.close();

            System.out.println("PDF файл успешно создан: " + fileName);
        } catch (IOException e) {
            System.err.println("Ошибка: Не удалось создать PDF файл.");
            e.printStackTrace();
        }
    }

    private static String getFontFilePath() {
        String systemRoot = System.getenv("SystemRoot");
        return systemRoot + "/Fonts/ARIALUNI.TTF";
    }

    private static void addUserInfoToDocument(Document document, User user, double currentBMI, double optimalWeight) {
        document.add(new Paragraph("Особиста інформація користувача: ")
                .setFontColor(ColorConstants.BLACK));

        document.add(new Paragraph("Ім'я: " + user.getNameUser())
                .setFontColor(ColorConstants.BLACK));
        document.add(new Paragraph("Номер телефона: " + user.getPhoneNumber())
                .setFontColor(ColorConstants.BLACK));
        document.add(new Paragraph("Вага зараз: " + user.getWeightUser())
                .setFontColor(ColorConstants.BLACK));
        document.add(new Paragraph("Оптимальна вага: " + optimalWeight)
                .setFontColor(ColorConstants.BLACK));
        document.add(new Paragraph("ІМТ зараз: " + currentBMI)
                .setFontColor(ColorConstants.BLACK));
    }

    private void addBMITableToDocument(Document document, double currentWeight, double optimalWeight, double height) {
        // Создаем таблицу
        Table table = new Table(3);
        table.setWidth(UnitValue.createPercentValue(100)); // Устанавливаем ширину таблицы на 100% ширины листа
        table.setFontSize(FONT_SIZE);

        table.addCell("Діапазон ІМТ");
        table.addCell("Значення уровня ІМТ");
        table.addCell("Діапазон ваги для користувача");

        // Добавляем строки с данными
        double weight = currentWeight;
        String currentBMIlevel = "";
        String bmiRange = "";
        String weightRangeStart = "";
        String weightRangeEnd = "";
        boolean isStartWeightSet = false;
        while (weight >= optimalWeight) {
            double bmi = loseWeightCalculator.calculateBMI(weight, height);
            String bmiLevel = calculateBMIlevel(bmi);

            if (!bmiLevel.equals(currentBMIlevel)) {
                if (!currentBMIlevel.isEmpty()) {
                    table.addCell(bmiRange).setFontColor(ColorConstants.BLACK);
                    table.addCell(currentBMIlevel).setFontColor(ColorConstants.BLACK);
                    table.addCell(weightRangeStart + " - " + weightRangeEnd).setFontColor(ColorConstants.BLACK);
                }

                currentBMIlevel = bmiLevel;
                bmiRange = getBMIrange(bmi);
                weightRangeStart = String.valueOf(weight);
                weightRangeEnd = String.valueOf(weight);
                isStartWeightSet = true;
            } else {
                weightRangeStart = String.valueOf(weight);
            }

            if (isStartWeightSet) {
                weight--;
            } else {
                weight -= 0.1; // Если начальный вес не был установлен, уменьшаем вес на 0.1 кг
            }
        }

        // Добавляем последнюю строку
        table.addCell(bmiRange).setFontColor(ColorConstants.BLACK);
        table.addCell(currentBMIlevel).setFontColor(ColorConstants.BLACK);
        table.addCell(weightRangeEnd + " - " + weightRangeStart).setFontColor(ColorConstants.BLACK);

        // Добавляем таблицу в документ
        document.add(table);
    }


    private static String calculateBMIlevel(double bmi) {
        if (bmi < 18.5) {
            return "Недостатня вага";
        } else if (bmi >= 18.5 && bmi < 24.9) {
            return "Нормальна вага";
        } else if (bmi >= 25 && bmi < 29.9) {
            return "Надлишкова вага";
        } else {
            return "Ожиріння";
        }
    }

    private static String getBMIrange(double bmi) {
        if (bmi < 18.5) {
            return "< 18.5";
        } else if (bmi >= 18.5 && bmi < 24.9) {
            return "18.5 - 24.9";
        } else if (bmi >= 25 && bmi < 29.9) {
            return "25 - 29.9";
        } else {
            return "30+";
        }
    }

    private LineChart<Number, Number> createBMIChart(double initialWeight, double optimalWeight, User user) {
        // Создаем оси графика
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Неделя");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("ИМТ");

        // Создаем график
        LineChart<Number, Number> bmiChart = new LineChart<>(xAxis, yAxis);
        bmiChart.setTitle("Прогноз изменения ИМТ");

        // Создаем серию данных для графика
        XYChart.Series<Number, Number> bmiSeries = new XYChart.Series<>();
        bmiSeries.setName("ИМТ");

        // Добавляем данные для каждой недели
        double currentWeight = initialWeight;
        double height = user.getHeightUser();
        for (int week = 0; week <= 100; week++) {
            double bmi = calculateBMI(currentWeight, height);
            bmiSeries.getData().add(new XYChart.Data<>(week, bmi));

            // Проверяем, достиг ли ИМТ текущего веса ИМТ оптимального веса с погрешностью
            if (Math.abs(bmi - calculateBMI(optimalWeight, height)) <= 0.1) {
                break;
            }

            // Рассчитываем следующий вес на основе дефицита калорий
            double deficitCaloriesPerWeek = LoseWeightCalculator.calculatorDeficitCaloric(currentWeight, height, user.getActivityLevel(), user.getAgeUser(), user.isGenderUser());
            double weightLossPerWeek = deficitCaloriesPerWeek / 7000; // 1 кг жира ~ 7000 калорий
            currentWeight -= weightLossPerWeek;
        }

        // Добавляем серию данных на график
        bmiChart.getData().add(bmiSeries);

        return bmiChart;
    }
    // Метод для расчета ИМТ
    public double calculateBMI(double weight, double height) {
        double dmi = LoseWeightCalculator.calculateBMI(weight, height);
        return  dmi; // Примерный возвращаемый результат
    }



    // Метод для создания объекта Graphics2D
    private Graphics2D createGraphics2D(WritableImage image) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        return bufferedImage.createGraphics();
    }

    private static void saveDocument(PDDocument document, String filePath, String fileName) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(fileName);
        fileChooser.setInitialDirectory(new File(filePath));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                document.save(outputStream);
            }
        }
    }
}