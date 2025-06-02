package guides;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class ColorsConfigGuideGenerator {

    public static void main(String[] args) {
        String outputPath = "ColorsConfig_Guide.docx";

        try (XWPFDocument doc = new XWPFDocument()) {

            // Title
            addStyledParagraph(doc, "🎨 Simple User Guide for colors-config.xlsx", 20, true, "6A0DAD");

            // Intro
            addStyledParagraph(doc, "📁 File Location", 14, true, "007377");
            addBodyText(doc, "You can find the colors-config.xlsx file inside the project folder at: \"MyProject/configurations\"");

            // Purpose
            addStyledParagraph(doc, "🧾 What is this sheet for?", 14, true, "007377");
            addBodyText(doc, "This Excel sheet helps you customize colors used in your project labels. "
                    + "You can either stick with the default colors or select your own favorites!");

            // How to Use It
            addStyledParagraph(doc, "⚙️ How to Use It?", 14, true, "007377");

            addBodyText(doc, "1️⃣ Use Default Colors or Not?");
            addBodyText(doc, "- Find the row labeled **USE_DEFAULTS** near the top.");
            addBodyText(doc, "- Choose TRUE to apply default colors automatically.");
            addBodyText(doc, "- Choose FALSE to pick your own custom colors.");

            addBodyText(doc, "2️⃣ Pick Your Colors");
            addBodyText(doc, "- Below the USE_DEFAULTS row, you’ll see keys like **DELETED**, **ADDED**, etc.");
            addBodyText(doc, "- If USE_DEFAULTS is FALSE, click the dropdown beside each key to select a color.");
            addBodyText(doc, "- If USE_DEFAULTS is TRUE, your picks here won’t be applied.");

            addBodyText(doc, "3️⃣ Watch for Warnings ⚠️");
            addBodyText(doc, "- If USE_DEFAULTS is TRUE but you’ve changed colors, a warning message will appear on the right.");
            addBodyText(doc, "- The warning reminds you to switch USE_DEFAULTS to FALSE or fix the colors.");

            addBodyText(doc, "4️⃣ Sheet Protection 🔒");
            addBodyText(doc, "- Some parts of the sheet are locked to prevent accidental changes.");
            addBodyText(doc, "- You can only edit USE_DEFAULTS and select colors from dropdowns.");

            addBodyText(doc, "5️⃣ Save Your Changes 💾");
            addBodyText(doc, "- Once done, save the file.");
            addBodyText(doc, "- Your project will read your selections and apply the colors accordingly.");

            // Tips
            addStyledParagraph(doc, "💡 Quick Tips", 14, true, "007377");
            addBodyText(doc, "• Use dropdowns — no need to type colors manually.");
            addBodyText(doc, "• Set USE_DEFAULTS to FALSE if you want custom colors.");
            addBodyText(doc, "• Keep it TRUE for easy default color management.");
            addBodyText(doc, "• Warning messages help prevent mismatches.");

            // Closing
            addBodyText(doc, "\nThat’s it! This guide helps you easily control the color settings for your project with minimal effort.");

            // Output
            try (FileOutputStream out = new FileOutputStream(outputPath)) {
                doc.write(out);
                System.out.println("✅ Colors config guide generated at: " + outputPath);
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
}
