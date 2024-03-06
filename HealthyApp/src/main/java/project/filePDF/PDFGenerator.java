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
import project.model.CustomMenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.Color;

@Slf4j
public class PDFGenerator {
    private static final int SIZE_TEXT = 12;
    private static final int SIZE_PAPER_X = 100;
    private static final int SIZE_PAPER_Y = 700;
    private static final int SIZE_LINE_Y = -20;
    private static final int MAX_PAPER_IN_LIST = 32;

    public static void createPDF(ObservableList<CustomMenuItem> itemsForPDF, String filePath, String fileName) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                initializeDocumentHeader(contentStream, document);


                int day = 1;
                boolean isFirstMenuOfDay = true;
                int currentLine = 0; // Счетчик строк на странице

                for (CustomMenuItem item : itemsForPDF) {
                    // Проверяем, достигли ли  предела строк на странице
                    if (currentLine >= MAX_PAPER_IN_LIST) {
                        addNewPage(document, contentStream);
                        currentLine = 0;
                    }

                    if (isFirstMenuOfDay) {
                        addDayHeader(contentStream, day);
                        currentLine++;
                        isFirstMenuOfDay = false;
                    }

                    addText(contentStream,document,  Color.BLUE, "Прийом їжі: ");
                    addText(contentStream,document,  Color.BLACK, item.getMealType());
                    currentLine++;

                    addText(contentStream,document,  Color.BLACK, "Назва: ");
                    addText(contentStream, document, Color.BLACK, item.getName());
                    currentLine++;

                    addText(contentStream,document,  Color.BLACK, "Кількість: ");
                    addText(contentStream, document, Color.BLACK, String.valueOf(item.getQuantity() + " г"));
                    currentLine++;

                    if (item.isLastMenuOfDay()) {
                        day++;
                        isFirstMenuOfDay = true;
                    }
                }
            }

            saveDocument(document, filePath, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeDocumentHeader(PDPageContentStream contentStream, PDDocument document) throws IOException {
        PDFont font = PDType0Font.load(document, new FileInputStream(getFontFile()));
        contentStream.setFont(font, SIZE_TEXT);
        contentStream.beginText();
        contentStream.newLineAtOffset(SIZE_PAPER_X, SIZE_PAPER_Y);
        contentStream.showText("Сгенероване меню програмою HealthyDiary");
        contentStream.newLineAtOffset(0, SIZE_LINE_Y);
    }

    private static void addNewPage(PDDocument document, PDPageContentStream contentStream) throws IOException {
        contentStream.endText();
        contentStream.close();

        PDPage page = new PDPage();
        document.addPage(page);

        contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(PDType0Font.load(document, new FileInputStream(getFontFile())), SIZE_TEXT);
        contentStream.beginText();
        contentStream.newLineAtOffset(SIZE_PAPER_X, SIZE_PAPER_Y);
    }

    private static void addDayHeader(PDPageContentStream contentStream, int day) throws IOException {
        contentStream.showText("День " + day + ":");
        contentStream.newLineAtOffset(0, SIZE_LINE_Y);
    }

    private static void addText(PDPageContentStream contentStream, PDDocument document, Color color, String text) throws IOException {
        contentStream.setNonStrokingColor(color);
        contentStream.setFont(PDType0Font.load(document, new FileInputStream(getFontFile())), SIZE_TEXT);
        contentStream.showText(text);
        contentStream.newLineAtOffset(0, SIZE_LINE_Y);
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

    private static File getFontFile() {
        String systemRoot = System.getenv("SystemRoot");
        return new File(systemRoot + "/Fonts/ARIALUNI.TTF");
    }
}