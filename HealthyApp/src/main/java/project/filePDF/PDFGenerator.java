package project.filePDF;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import javafx.collections.ObservableList;
import project.tableView.CustomMenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.Color;

@Slf4j
public class PDFGenerator {
    private static final int SIZE_TEXT = 12;
    private static final  int SIZE_PAPER_X = 100;
    private static final int SIZE_PAPER_Y = 700;
    private static final int SIZE_LINE_Y = -20;
    private static final int MAX_PAPER_IN_LIST = 32;
    public static void createPDF(ObservableList<CustomMenuItem> itemsForPDF, String filePath, String fileName) throws IOException {
        try {
            int day = 1;
            boolean isFirstMenuOfDay = true;
            int currentLine = 0; // Счетчик строк на странице

            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            Stage primaryStage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(fileName);
            fileChooser.setInitialDirectory(new File(filePath));

            // Устанавливаем фильтр для файлов PDF
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(primaryStage);

            String systemRoot = System.getenv("SystemRoot");
            File fontFile = new File(systemRoot + "/Fonts/ARIALUNI.TTF");
            PDFont font = PDType0Font.load(document, new FileInputStream(fontFile));
            contentStream.setFont(font, SIZE_TEXT);
            contentStream.beginText();
            contentStream.newLineAtOffset(SIZE_PAPER_X, SIZE_PAPER_Y);

            contentStream.showText("Сгенероване меню програмою HealthyDiary");
            contentStream.newLineAtOffset(0, SIZE_LINE_Y);


            for (CustomMenuItem item : itemsForPDF) {
                // Проверяем, достигли ли  предела строк на странице
                if (currentLine >= MAX_PAPER_IN_LIST) {
                    // Добавляем новую страницу
                    contentStream.endText();
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(font, SIZE_TEXT);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(SIZE_PAPER_X, SIZE_PAPER_Y);
                    currentLine = 0;
                }

                if (isFirstMenuOfDay) {
                    contentStream.showText("День " + day + ":");
                    contentStream.newLineAtOffset(0, SIZE_LINE_Y);
                    currentLine++;
                    isFirstMenuOfDay = false;
                }
                contentStream.setNonStrokingColor(Color.BLUE);
                contentStream.setFont(font, SIZE_TEXT);
                contentStream.showText("Прийом їжі: ");
                contentStream.showText(item.getMealType());
                contentStream.newLineAtOffset(0, SIZE_LINE_Y);
                currentLine++;

                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.setFont(font, SIZE_TEXT);
                contentStream.showText("Назва: ");
                contentStream.showText(item.getName());
                contentStream.newLineAtOffset(0, SIZE_LINE_Y);
                currentLine++;

                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.setFont(font, SIZE_TEXT);
                contentStream.showText("Кількість: ");
                contentStream.showText(String.valueOf(item.getQuantity() + " г"));
                contentStream.newLineAtOffset(0, SIZE_LINE_Y);
                currentLine++;

                if (item.isLastMenuOfDay()) {
                    day++;
                    isFirstMenuOfDay = true;
                }
            }
            contentStream.endText();
            contentStream.close();

            // Сохраняем PDF
            document.save(file);
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (SecurityException e) {
            log.error("Security exception: {} " , e.getMessage());
            e.printStackTrace();
        }
    }
}
