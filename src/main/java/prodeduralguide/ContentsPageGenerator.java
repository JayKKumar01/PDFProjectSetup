package prodeduralguide;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;


import java.math.BigInteger;

public class ContentsPageGenerator {

    public static void addContentsPage(XWPFDocument doc) {
        addSectionHeader(doc, "1. Getting Started");
        addIndentedBulletWithPageNumber(doc, "1.1 Introduction", 1, 3);
        addIndentedBulletWithPageNumber(doc, "1.2 Purpose", 1, 4);
        addIndentedBulletWithPageNumber(doc, "1.3 Target Audience", 1, 5);
        addIndentedBulletWithPageNumber(doc, "1.4 Prerequisites", 1, 6);

        addSectionHeader(doc, "2. Setup");
        addIndentedBulletWithPageNumber(doc, "2.1 Option 1: Automatic Setup", 1, 7);
        addIndentedBulletWithPageNumber(doc, "2.2 Option 2: Manual Setup", 1, 8);
        addIndentedBulletWithPageNumber(doc, "    2.2.1 Extract the Zip", 2, 9);
        addIndentedBulletWithPageNumber(doc, "    2.2.2 Place Files in Correct Locations", 2, 10);
        addIndentedBulletWithPageNumber(doc, "    2.2.3 Update Sample Input File", 2, 11);
        addIndentedBulletWithPageNumber(doc, "    2.2.4 Set Input Excel Path in Epsilon", 2, 12);

        addSectionHeader(doc, "3. User Guide");
        addIndentedBulletWithPageNumber(doc, "3.1 Understanding colors-config.xlsx", 1, 13);
        addIndentedBulletWithPageNumber(doc, "3.2 Understanding input-data-guide.docx", 1, 14);
        addIndentedBulletWithPageNumber(doc, "3.3 Common Troubleshooting Tips", 1, 15);

        addSectionHeader(doc, "4. Other");
        addIndentedBulletWithPageNumber(doc, "4.1 Notes", 1, 16);
        addIndentedBulletWithPageNumber(doc, "4.2 Contact & Support", 1, 17);
        addIndentedBulletWithPageNumber(doc, "4.3 License / Legal", 1, 18);
    }

    private static void addSectionHeader(XWPFDocument doc, String heading) {
        XWPFParagraph p = doc.createParagraph();
        p.setSpacingBefore(100);
        XWPFRun run = p.createRun();
        run.setText(heading);
        run.setBold(true);
        run.setFontSize(12);
        run.setFontFamily("Segoe UI");
    }

    private static void addIndentedBulletWithPageNumber(XWPFDocument doc, String text, int indentLevel, int pageNumber) {
        XWPFParagraph p = doc.createParagraph();

        // Set indentation left
        p.setIndentationLeft(indentLevel * 300);

        // Create and set a right-aligned tab stop with dotted leader
        CTP ctp = p.getCTP();
        CTPPr ppr = ctp.isSetPPr() ? ctp.getPPr() : ctp.addNewPPr();
        CTTabs tabs = ppr.isSetTabs() ? ppr.getTabs() : ppr.addNewTabs();
        CTTabStop tabStop = tabs.addNewTab();
        tabStop.setVal(STTabJc.RIGHT);
        tabStop.setLeader(STTabTlc.DOT);  // Dotted leader for dots
        tabStop.setPos(BigInteger.valueOf(9000));

        // Add the main text run
        XWPFRun run = p.createRun();
        run.setText(text);
        run.setFontSize(11);
        run.setFontFamily("Calibri");

        // Add a tab character to move to the right tab stop
        run.addTab();

        // Add the page number run (right aligned)
        XWPFRun pageNumRun = p.createRun();
        pageNumRun.setText(String.valueOf(pageNumber));
        pageNumRun.setFontSize(11);
        pageNumRun.setFontFamily("Calibri");
    }
}
