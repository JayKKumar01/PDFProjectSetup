package prodeduralguide;

import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class ProceduralGuideGenerator {

    // === Configurable variables ===
    private static final String OUTPUT_PATH = "Procedural_Guide.docx";
    private static final String PROJECT_NAME = "MyProject";
    private static final String CUSTOM_METHOD_JAR = "customMethod.jar";
    private static final String ZIP_FILE_NAME = PROJECT_NAME + "_RequiredFiles.zip";
    private static final String WORKSPACE_FOLDER_NAME = PROJECT_NAME + " Workspace";
    private static final String SAMPLE_EXCEL = "SampleInputData.xlsx";

    public static void main(String[] args) {
        try (XWPFDocument doc = new XWPFDocument()) {

            // Title Page (centered)
            addTitlePage(doc, "📘 Procedural Guide");

            // Page break before Contents page
            addPageBreak(doc);

            // Contents Page (assuming existing class)
            ContentsPageGenerator.addContentsPage(doc);

            // Page break before Getting Started page
            addPageBreak(doc);

            // Getting Started Page (assuming existing class)
            GettingStartedPage.addGettingStartedPage(doc, PROJECT_NAME);

            // Page break before Setup section
            addPageBreak(doc);

            // Add Setup Section using new SetupSectionGenerator
            SetupSectionGenerator.addSetupSection(doc,
                    PROJECT_NAME,
                    CUSTOM_METHOD_JAR,
                    ZIP_FILE_NAME,
                    WORKSPACE_FOLDER_NAME,
                    SAMPLE_EXCEL);

            // PAGE BREAK before User Guide section
            addPageBreak(doc);

            // Add User Guide Section here
            UserGuidePageGenerator.addUserGuidePage(doc, WORKSPACE_FOLDER_NAME);

            try (FileOutputStream out = new FileOutputStream(OUTPUT_PATH)) {
                doc.write(out);
                System.out.println("✅ Procedural guide generated at: " + OUTPUT_PATH);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addTitlePage(XWPFDocument doc, String titleText) {
        XWPFParagraph titleParagraph = doc.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        titleParagraph.setVerticalAlignment(TextAlignment.CENTER);

        XWPFRun run = titleParagraph.createRun();
        run.setText(titleText);
        run.setFontFamily("Segoe UI");
        run.setColor("004670");
        run.setBold(true);
        run.setFontSize(36);

        // Add spacing to roughly vertically center on the page
        titleParagraph.setSpacingBefore(600);
        titleParagraph.setSpacingAfter(600);
    }

    private static void addPageBreak(XWPFDocument doc) {
        XWPFParagraph pageBreak = doc.createParagraph();
        pageBreak.setPageBreak(true);
    }
}
