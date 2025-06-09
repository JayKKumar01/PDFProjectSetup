package guides;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class SetupGuideGenerator {

    public static void main(String[] args) {
        String projectName = "MyProject";
        String customMethodJarName = "customMethod.jar";
        String workspaceFolderName = projectName + " Workspace";
        String zipFileName = projectName + "_RequiredFiles.zip";
        String sampleExcel = "SampleInputData.xlsx";
        String outputPath = projectName + "_Setup_Guide.docx";

        try (XWPFDocument doc = new XWPFDocument()) {

            // Title
            addStyledParagraph(doc, "🚀 " + projectName + " Setup Guide", 20, true, "004670");

            // Automatic Setup
            addStyledParagraph(doc, "Option 1: Automatic Setup (Recommended)", 14, true, "000000");
            addBodyText(doc, "1. Extract the file: " + zipFileName);
            addBodyText(doc, "2. Double-click on the setup.jar file.");
            addBodyText(doc, "3. It will automatically place all files and folders in their correct locations.");
            addItalicText(doc, "✅ That’s it! You're ready to go. Open " + projectName + " in Epsilon.");

            // Manual Setup
            addStyledParagraph(doc, "Option 2: Manual Setup", 14, true, "000000");

            // Step 1
            addStyledParagraph(doc, "Step 1: Extract the Zip", 12, true, "000000");
            addBodyText(doc, "Unzip the file:");
            addCodeBlock(doc, zipFileName);
            addBodyText(doc, "It contains the following files and folders required for the setup:");

            String[][] contents = {
                    {customMethodJarName, "Custom command JAR to be placed manually"},
                    {projectName, "Project folder"},
                    {workspaceFolderName, "Workspace folder containing sample input"}
            };
            addTable(doc, contents, new String[]{"File/Folder", "Description"}, "D9D9D9");

            // Step 2
            addStyledParagraph(doc, "Step 2: Place Files in Correct Locations", 12, true, "000000");
            addBodyText(doc, "Use the following paths (replace <YourUsername> with your Windows username):");

            String[][] paths = {
                    {customMethodJarName, "C:\\Users\\<YourUsername>\\.epsilon\\lib\\commands"},
                    {projectName, "C:\\Users\\<YourUsername>\\.epsilon\\Projects"},
                    {workspaceFolderName, "C:\\Users\\<YourUsername>\\.epsilon"}
            };
            addTable(doc, paths, new String[]{"Item", "Destination Path"}, "F2F2F2");
            addItalicText(doc, "🛠 Tip: If you don’t see the .epsilon folder, enable hidden files in File Explorer.");

            // Step 3
            addStyledParagraph(doc, "Step 3: Update Sample Input File", 12, true, "000000");
            addBodyText(doc, "Go to:");
            addCodeBlock(doc, "C:\\Users\\<YourUsername>\\.epsilon\\" + workspaceFolderName + "\\" + sampleExcel);
            addBodyText(doc,
                    "Inside this Excel file:\n- Replace all document paths containing \"JAKUMA5\" with your actual Windows username.\n\nExample:");
            addCodeBlock(doc, "C:\\Users\\JAKUMA5\\Documents\\MyFile.docx");
            addBodyText(doc, "Change to:");
            addCodeBlock(doc, "C:\\Users\\YourUsername\\Documents\\MyFile.docx");

            // Step 4
            addStyledParagraph(doc, "Step 4: Set Input Excel File in Epsilon", 12, true, "000000");
            addBodyText(doc, "Open the Epsilon tool.");
            addBodyText(doc, "Look for the field labeled \"InputData\".");
            addBodyText(doc, "Paste the full path to the updated " + sampleExcel + " into that field.");
            addCodeBlock(doc, "C:\\Users\\YourUsername\\.epsilon\\" + workspaceFolderName + "\\" + sampleExcel);

            // Notes
            addStyledParagraph(doc, "📝 Notes", 14, true, "000000");
            addBodyText(doc, "- Make sure Java is installed to run the tools.");
            addBodyText(doc, "- Do not rename any folders before placing them.");
            addBodyText(doc, "- Restart any related IDE or tools after setup.");

            // Done
            addStyledParagraph(doc, "🎉 Done!", 14, true, "000000");
            addBodyText(doc, "You’re now fully set up to use " + projectName +
                    ". If you face any issues, reach out to the project maintainer.");

            try (FileOutputStream out = new FileOutputStream(outputPath)) {
                doc.write(out);
                System.out.println("✅ Word setup guide generated at: " + outputPath);
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

    private static void addItalicText(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        XWPFRun run = p.createRun();
        run.setText(text);
        run.setFontFamily("Open Sans");
        run.setFontSize(11);
        run.setItalic(true);
    }

    private static void addCodeBlock(XWPFDocument doc, String code) {
        XWPFParagraph p = doc.createParagraph();
        p.setSpacingAfter(100);
        XWPFRun run = p.createRun();
        run.setText(code);
        run.setFontFamily("Consolas");
        run.setFontSize(10);
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
