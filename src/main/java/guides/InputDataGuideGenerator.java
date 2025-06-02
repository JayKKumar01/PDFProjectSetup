package guides;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class InputDataGuideGenerator {

    public static void main(String[] args) {
        String outputPath = "InputData_Guide.docx";

        try (XWPFDocument doc = new XWPFDocument()) {

            // Title
            addStyledParagraph(doc, "📘 Input Data Excel Sheet Setup Guide", 20, true, "004670");

            // Introduction
            addStyledParagraph(doc, "🔹 Introduction", 14, true, "007377");
            addBodyText(doc, "This guide helps you understand how to fill the Input Data Excel sheet used by the Epsilon framework. "
                    + "This sheet tells the system which documents to compare and which pages to use.");

            // Purpose
            addStyledParagraph(doc, "🎯 Purpose", 14, true, "007377");
            addBodyText(doc, "You can name your Excel file anything, but it must follow the structure explained below. "
                    + "This sheet is used to define test cases, which will show up in the InteractiveReport's left menu after you run the project.");

            // Structure Overview
            addStyledParagraph(doc, "📊 Sheet Structure Overview", 14, true, "007377");

            String[][] structure = {
                    {"TestCases", "Name that appears in the report's side menu"},
                    {"Source", "Path to the first file (Word or PDF)"},
                    {"Destination", "Path to the second file (Word or PDF)"},
                    {"FormsPageRange#1", "Page numbers to extract from the Source document (e.g., 1-3, 5)"},
                    {"FormsPageRange#2", "Page numbers to extract from the Destination document (e.g., 2, 4-6)"}
            };
            addTable(doc, structure, new String[]{"Column Name", "Description"}, "D9D9D9");

            // Notes
            addStyledParagraph(doc, "📌 Important Notes", 14, true, "007377");
            addBodyText(doc, "- You can add as many test cases (rows) as needed.");
            addBodyText(doc, "- Page ranges are optional. If left blank, all pages will be used.");
            addBodyText(doc, "- Use commas to separate multiple ranges: 2-3, 5, 7-9");
            addBodyText(doc, "- You can compare PDF to Word, Word to PDF, PDF to PDF, or Word to Word.");
            addBodyText(doc, "- The column headers must be exactly as shown.");

            // Example
            addStyledParagraph(doc, "🧾 Example", 14, true, "007377");

            String[][] example = {
                    {"Test Case 1", "C:/docs/file1.pdf", "C:/docs/file2.docx", "2-3, 5", "4-6"},
                    {"Test Case 2", "source.docx", "target.pdf", "1, 3-4", "2-5"},
                    {"Test Case 3", "docA.pdf", "docB.pdf", "", ""}
            };
            addTable(doc, example, new String[]{"TestCases", "Source", "Destination", "FormsPageRange#1", "FormsPageRange#2"}, "F2F2F2");

            // Usage Flow
            addStyledParagraph(doc, "▶️ How to Use This Sheet", 14, true, "007377");
            addBodyText(doc, "1. Create or edit your Excel sheet using the structure above.");
            addBodyText(doc, "2. Fill in each row to define a test case.");
            addBodyText(doc, "3. Save the Excel file where the Epsilon project can access it.");
            addBodyText(doc, "4. In your Epsilon project, set the full file path to this Excel sheet in the variable named InputData.");
            addBodyText(doc, "5. Run the project.");
            addBodyText(doc, "6. The InteractiveReport will open and list your test cases in the left menu.");
            addBodyText(doc, "7. Click on any test case to see side-by-side document comparisons based on the page ranges you provided.");

            // Output file
            try (FileOutputStream out = new FileOutputStream(outputPath)) {
                doc.write(out);
                System.out.println("✅ Input data guide generated at: " + outputPath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // === Styling Helpers ===

    private static void addStyledParagraph(XWPFDocument doc, String text, int fontSize, boolean bold, String hexColor) {
        XWPFParagraph p = doc.createParagraph();
        p.setSpacingAfter(100);
        XWPFRun run = p.createRun();
        run.setText(text);
        run.setFontSize(fontSize);
        run.setBold(bold);
        run.setFontFamily("Segoe UI");
        run.setColor(hexColor);
    }

    private static void addBodyText(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        p.setSpacingAfter(60);
        XWPFRun run = p.createRun();
        run.setText(text);
        run.setFontFamily("Calibri");
        run.setFontSize(11);
    }

    private static void addTable(XWPFDocument doc, String[][] data, String[] headers, String headerColorHex) {
        int rows = data.length + 1;
        int cols = headers.length;

        XWPFTable table = doc.createTable(rows, cols);
        table.setWidth("100%");

        // Header row
        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < cols; i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            XWPFParagraph para = cell.getParagraphs().get(0);
            para.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun run = para.createRun();
            run.setText(headers[i]);
            run.setBold(true);
            run.setFontFamily("Segoe UI");
            run.setFontSize(11);
            setCellColor(cell, headerColorHex);
        }

        // Data rows
        for (int i = 0; i < data.length; i++) {
            XWPFTableRow row = table.getRow(i + 1);
            for (int j = 0; j < cols; j++) {
                XWPFTableCell cell = row.getCell(j);
                XWPFParagraph para = cell.getParagraphs().get(0);
                para.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun run = para.createRun();
                run.setText(data[i][j]);
                run.setFontFamily("Calibri");
                run.setFontSize(10);
            }
        }
    }

    private static void setCellColor(XWPFTableCell cell, String hexColor) {
        CTTcPr tcPr = cell.getCTTc().isSetTcPr() ? cell.getCTTc().getTcPr() : cell.getCTTc().addNewTcPr();
        CTShd ctshd = tcPr.isSetShd() ? tcPr.getShd() : tcPr.addNewShd();
        ctshd.setFill(hexColor);
    }
}
