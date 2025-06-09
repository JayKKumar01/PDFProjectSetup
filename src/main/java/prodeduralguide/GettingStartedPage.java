package prodeduralguide;

import org.apache.poi.xwpf.usermodel.*;

public class GettingStartedPage {

    public static void addGettingStartedPage(XWPFDocument doc, String projectName) {
        addTitle(doc, "Getting Started");

        addSection(doc, "1.1 Introduction",
            projectName + " is designed to compare Word and PDF documents by analysing both the textual content and font formatting details such as font name, font size, and font style. " +
            "It generates an interactive report with color-coded boxes to highlight changes."
        );

        addSection(doc, "1.2 Purpose",
            "This project aims to simplify document comparison by providing clear visual differences and layout validation, enabling users to easily verify document updates."
        );

        addSection(doc, "1.3 Target Audience",
            "This tool is intended for document reviewers, QA engineers, and developers who need accurate comparison of Word and PDF documents."
        );

        addSectionWithBullets(doc, "1.4 Prerequisites", new String[]{
            "Java 17 or higher must be installed",
            "Epsilon software should be installed (see Epsilon - Access & Downloads in The Village)",
            "Basic understanding of document formats and validation processes"
        });
    }

    private static void addTitle(XWPFDocument doc, String text) {
        XWPFParagraph title = doc.createParagraph();
        title.setSpacingBefore(300);
        title.setSpacingAfter(300);
        XWPFRun run = title.createRun();
        run.setText(text);
        run.setBold(true);
        run.setFontSize(18);
        run.setFontFamily("Segoe UI");
        run.setColor("004670");  // dark blue
    }

    private static void addSection(XWPFDocument doc, String heading, String body) {
        // Heading (e.g., 1.1 Introduction)
        XWPFParagraph headingPara = doc.createParagraph();
        headingPara.setSpacingBefore(200);
        headingPara.setSpacingAfter(100);
        XWPFRun headingRun = headingPara.createRun();
        headingRun.setText(heading);
        headingRun.setBold(true);
        headingRun.setFontSize(14);
        headingRun.setFontFamily("Segoe UI");
        headingRun.setColor("007377");  // teal color

        // Body paragraph
        XWPFParagraph bodyPara = doc.createParagraph();
        bodyPara.setSpacingAfter(200);
        XWPFRun bodyRun = bodyPara.createRun();
        bodyRun.setText(body);
        bodyRun.setFontSize(11);
        bodyRun.setFontFamily("Calibri");
    }

    private static void addSectionWithBullets(XWPFDocument doc, String heading, String[] bullets) {
        // Heading
        XWPFParagraph headingPara = doc.createParagraph();
        headingPara.setSpacingBefore(200);
        headingPara.setSpacingAfter(100);
        XWPFRun headingRun = headingPara.createRun();
        headingRun.setText(heading);
        headingRun.setBold(true);
        headingRun.setFontSize(14);
        headingRun.setFontFamily("Segoe UI");
        headingRun.setColor("007377");

        // Bullets (indented text with dash)
        for (String bullet : bullets) {
            XWPFParagraph bulletPara = doc.createParagraph();
            bulletPara.setIndentationLeft(500);
            bulletPara.setSpacingAfter(100);
            XWPFRun run = bulletPara.createRun();
            run.setText("• " + bullet);
            run.setFontSize(11);
            run.setFontFamily("Calibri");
        }
    }
}
