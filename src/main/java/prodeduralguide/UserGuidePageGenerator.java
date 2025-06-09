package prodeduralguide;

import org.apache.poi.xwpf.usermodel.*;

public class UserGuidePageGenerator {

    public static void addUserGuidePage(XWPFDocument doc, String workspaceFolderName) {

        // === Section title ===
        addStyledHeading(doc, "3. User Guide", 20, "004670", 200);

        // === 3.1 Understanding colors-config.xlsx ===
        addStyledHeading(doc, "3.1 Understanding colors-config.xlsx", 14, "007377", 100);
        ColorsConfigGuideSection.addDetailedSection(doc);  // Add full guide directly

        addPageBreak(doc);

        // === 3.2 Understanding input-data-guide.docx ===
        addStyledHeading(doc, "3.2 Understanding input-data-guide.docx", 14, "007377", 100);
        InputDataGuideSection.addDetailedSection(doc);  // Add full guide directly

        addPageBreak(doc);

        // === 3.3 Common Troubleshooting Tips ===
        addStyledHeading(doc, "3.3 Common Troubleshooting Tips", 14, "007377", 100);
        addBulletPoint(doc, "Ensure all input files are correctly placed in the appropriate workspace folders.");
        addBulletPoint(doc, "Check that filenames and extensions match the expected formats.");
        addBulletPoint(doc, "Make sure Excel sheet formatting matches the guide specifications.");
        addBulletPoint(doc, "If issues persist, contact the support team for assistance.");
    }
    private static void addPageBreak(XWPFDocument doc) {
        XWPFParagraph pageBreak = doc.createParagraph();
        pageBreak.setPageBreak(true);
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

    private static void addBulletPoint(XWPFDocument doc, String bulletText) {
        XWPFParagraph bullet = doc.createParagraph();
        bullet.setSpacingAfter(40);
        bullet.setStyle("ListParagraph");
        XWPFRun run = bullet.createRun();
        run.setText("• " + bulletText);
        run.setFontFamily("Calibri");
        run.setFontSize(11);
    }
}
