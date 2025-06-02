package guides;

import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class EmailWordGenerator {

    // === Configurable Variables ===
    static String clientName = "[Client's Name]";
    static String projectName = "EpsilonDocumentCompare";
    static String zipFileName = "EpsilonDocumentCompare_RequiredFiles.zip";
    static String setupGuideFileName = "Project Setup Guide";
    static String yourFullName = "[Your Full Name]";
    static String yourPosition = "[Your Position]";
    static String yourCompany = "[Your Company Name]";
    static String yourContactInfo = "[Your Contact Information]";
    static String outputFileName = "EmailToClient.docx";

    public static void main(String[] args) {
        try (XWPFDocument doc = new XWPFDocument()) {

            // Title - Subject line
            XWPFParagraph title = doc.createParagraph();
            title.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun runTitle = title.createRun();
            runTitle.setBold(true);
            runTitle.setFontFamily("Calibri");
            runTitle.setFontSize(16);
            runTitle.setText("Subject: " + projectName + " – Project Setup Guide and Required Files");
            runTitle.addBreak();
            runTitle.addBreak();

            // Greeting
            XWPFParagraph greeting = doc.createParagraph();
            XWPFRun runGreeting = greeting.createRun();
            runGreeting.setFontFamily("Calibri");
            runGreeting.setFontSize(12);
            runGreeting.setText("Dear " + clientName + ",");
            runGreeting.addBreak();
            runGreeting.addBreak();

            // Opening sentence
            XWPFParagraph opening = doc.createParagraph();
            XWPFRun runOpening = opening.createRun();
            runOpening.setFontFamily("Calibri");
            runOpening.setFontSize(12);
            runOpening.setText("I hope this message finds you well.");
            runOpening.addBreak();
            runOpening.addBreak();

            // Attachments section
            XWPFParagraph attachmentsIntro = doc.createParagraph();
            XWPFRun runAttachmentsIntro = attachmentsIntro.createRun();
            runAttachmentsIntro.setFontFamily("Calibri");
            runAttachmentsIntro.setFontSize(12);
            runAttachmentsIntro.setText("Please find attached two important files to help you get started with the " + projectName + " project:");
            runAttachmentsIntro.addBreak();
            runAttachmentsIntro.addBreak();

            // Attachment 1
            XWPFParagraph attachment1 = doc.createParagraph();
            XWPFRun runAttachment1 = attachment1.createRun();
            runAttachment1.setFontFamily("Calibri");
            runAttachment1.setFontSize(12);
            runAttachment1.setBold(true);
            runAttachment1.setText("- " + setupGuideFileName + ": ");
            runAttachment1.setBold(false);
            runAttachment1.setText("This document provides detailed instructions about the project structure, configuration, and initial steps. Please review this guide carefully before proceeding.");
            runAttachment1.addBreak();

            // Attachment 2
            XWPFParagraph attachment2 = doc.createParagraph();
            XWPFRun runAttachment2 = attachment2.createRun();
            runAttachment2.setFontFamily("Calibri");
            runAttachment2.setFontSize(12);
            runAttachment2.setBold(true);
            runAttachment2.setText("- " + zipFileName + ": ");
            runAttachment2.setBold(false);
            runAttachment2.setText("This archive contains all the necessary files and folders to run the project smoothly.");
            runAttachment2.addBreak();
            runAttachment2.addBreak();

            // Project Overview
            XWPFParagraph overviewTitle = doc.createParagraph();
            XWPFRun runOverviewTitle = overviewTitle.createRun();
            runOverviewTitle.setFontFamily("Calibri");
            runOverviewTitle.setFontSize(12);
            runOverviewTitle.setBold(true);
            runOverviewTitle.setText("Project Overview:");
            runOverviewTitle.addBreak();

            XWPFParagraph overview = doc.createParagraph();
            XWPFRun runOverview = overview.createRun();
            runOverview.setFontFamily("Calibri");
            runOverview.setFontSize(12);
            runOverview.setText(projectName + " is designed to compare Word and PDF documents by analyzing both the textual content and font formatting details such as font name, font size, and font style.");
            runOverview.addBreak();
            runOverview.setText("It generates an interactive report where differences are clearly highlighted with color-coded boxes to help users easily understand the type of changes (e.g., added, deleted, or formatting changes).");
            runOverview.addBreak();
            runOverview.addBreak();

            // Alignment Validation
            XWPFParagraph alignmentTitle = doc.createParagraph();
            XWPFRun runAlignmentTitle = alignmentTitle.createRun();
            runAlignmentTitle.setFontFamily("Calibri");
            runAlignmentTitle.setFontSize(12);
            runAlignmentTitle.setBold(true);
            runAlignmentTitle.setText("Alignment Validation:");
            runAlignmentTitle.addBreak();

            XWPFParagraph alignment = doc.createParagraph();
            XWPFRun runAlignment = alignment.createRun();
            runAlignment.setFontFamily("Calibri");
            runAlignment.setFontSize(12);
            runAlignment.setText("The project includes an alignment validation feature accessible within the report. Users can toggle between content validation and alignment validation views.");
            runAlignment.addBreak();
            runAlignment.setText("This feature includes:");
            runAlignment.addBreak();
            runAlignment.setText("• Press and Hold: Quickly switch to the next document view for easy comparison of layout differences.");
            runAlignment.addBreak();
            runAlignment.setText("• Highlight: Marks pixel-level differences to accurately show alignment shifts.");
            runAlignment.addBreak();
            runAlignment.addBreak();

            // Closing
            XWPFParagraph closing = doc.createParagraph();
            XWPFRun runClosing = closing.createRun();
            runClosing.setFontFamily("Calibri");
            runClosing.setFontSize(12);
            runClosing.setText("The setup guide will help you utilize these features effectively and complete the project setup without issues.");
            runClosing.addBreak();
            runClosing.setText("Should you have any questions or require assistance, please do not hesitate to contact me.");
            runClosing.addBreak();
            runClosing.addBreak();

            XWPFParagraph thanks = doc.createParagraph();
            XWPFRun runThanks = thanks.createRun();
            runThanks.setFontFamily("Calibri");
            runThanks.setFontSize(12);
            runThanks.setText("Thank you for your time, and I look forward to your feedback.");
            runThanks.addBreak();
            runThanks.addBreak();

            // Signature
            XWPFParagraph signature = doc.createParagraph();
            XWPFRun runSignature = signature.createRun();
            runSignature.setFontFamily("Calibri");
            runSignature.setFontSize(12);
            runSignature.setText("Best regards,");
            runSignature.addBreak();
            runSignature.setText(yourFullName);
            runSignature.addBreak();
            runSignature.setText(yourPosition);
            runSignature.addBreak();
            runSignature.setText(yourCompany);
            runSignature.addBreak();
            runSignature.setText(yourContactInfo);

            // Write to file
            try (FileOutputStream out = new FileOutputStream(outputFileName)) {
                doc.write(out);
                System.out.println("Word document generated successfully: " + outputFileName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
