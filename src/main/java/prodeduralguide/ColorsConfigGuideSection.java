package prodeduralguide;

import org.apache.poi.xwpf.usermodel.*;

public class ColorsConfigGuideSection {

    public static void addDetailedSection(XWPFDocument doc) {
        // 📁 File Location Note
        addStyledHeading(doc, "📁 Where to Find color-config.xlsx?", 13, "2F5496", 100);
        addBodyText(doc, "The color-config.xlsx file is located inside the configurations folder in your workspace. You can open and edit this file to customize the color settings as described below.");

        // 🧾 What is this sheet for?
        addStyledHeading(doc, "🧾 What is this sheet for?", 13, "2F5496", 100);
        addBodyText(doc, "This Excel sheet helps you customize colors used in your project labels. You can either stick with the default colors or select your own favorites!");

        // ⚙️ How to Use It?
        addStyledHeading(doc, "⚙️ How to Use It?", 13, "2F5496", 100);

        // 1️⃣ Use Default Colors or Not?
        addSubHeading(doc, "1️⃣ Use Default Colors or Not?");
        addBullet(doc, "Find the row labeled USE_DEFAULTS near the top.");
        addBullet(doc, "Choose TRUE to apply default colors automatically.");
        addBullet(doc, "Choose FALSE to pick your own custom colors.");

        // 2️⃣ Pick Your Colors
        addSubHeading(doc, "2️⃣ Pick Your Colors");
        addBullet(doc, "Below the USE_DEFAULTS row, you’ll see keys like DELETED, ADDED, etc.");
        addBullet(doc, "If USE_DEFAULTS is FALSE, click the dropdown beside each key to select a color.");
        addBullet(doc, "If USE_DEFAULTS is TRUE, your picks here won’t be applied.");

        // 3️⃣ Watch for Warnings ⚠️
        addSubHeading(doc, "3️⃣ Watch for Warnings ⚠️");
        addBullet(doc, "If USE_DEFAULTS is TRUE but you’ve changed colors, a warning message will appear on the right.");
        addBullet(doc, "The warning reminds you to switch USE_DEFAULTS to FALSE or fix the colors.");

        // 4️⃣ Sheet Protection 🔒
        addSubHeading(doc, "4️⃣ Sheet Protection 🔒");
        addBullet(doc, "Some parts of the sheet are locked to prevent accidental changes.");
        addBullet(doc, "You can only edit USE_DEFAULTS and select colors from dropdowns.");

        // 5️⃣ Save Your Changes 💾
        addSubHeading(doc, "5️⃣ Save Your Changes 💾");
        addBullet(doc, "Once done, save the file.");
        addBullet(doc, "Your project will read your selections and apply the colors accordingly.");

        // 💡 Quick Tips
        addStyledHeading(doc, "💡 Quick Tips", 13, "2F5496", 80);
        addBullet(doc, "Use dropdowns — no need to type colors manually.");
        addBullet(doc, "Set USE_DEFAULTS to FALSE if you want custom colors.");
        addBullet(doc, "Keep it TRUE for easy default color management.");
        addBullet(doc, "Warning messages help prevent mismatches.");

        // Ending note
        addBodyText(doc, "That’s it! This guide helps you easily control the color settings for your project with minimal effort.");
    }

    private static void addStyledHeading(XWPFDocument doc, String text, int fontSize, String color, int spacingAfter) {
        XWPFParagraph paragraph = doc.createParagraph();
        paragraph.setSpacingAfter(spacingAfter);
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setFontSize(fontSize);
        run.setBold(true);
        run.setFontFamily("Segoe UI");
        run.setColor(color);
    }

    private static void addSubHeading(XWPFDocument doc, String text) {
        XWPFParagraph paragraph = doc.createParagraph();
        paragraph.setSpacingAfter(50);
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setFontSize(12);
        run.setBold(true);
        run.setFontFamily("Segoe UI");
        run.setColor("2E75B6");
    }

    private static void addBullet(XWPFDocument doc, String bulletText) {
        XWPFParagraph bullet = doc.createParagraph();
        bullet.setSpacingAfter(40);
        XWPFRun run = bullet.createRun();
        run.setText("• " + bulletText);
        run.setFontFamily("Calibri");
        run.setFontSize(11);
    }

    private static void addBodyText(XWPFDocument doc, String text) {
        XWPFParagraph paragraph = doc.createParagraph();
        paragraph.setSpacingAfter(80);
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setFontFamily("Calibri");
        run.setFontSize(11);
    }
}
