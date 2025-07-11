package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ColorConfigExcelCreator {

    private static final LinkedHashMap<String, String> DEFAULT_COLORS = new LinkedHashMap<>() {{
        put("DELETED", "RED");
        put("ADDED", "GOLD");
        put("FONT_NAME", "MAGENTA");
        put("FONT_SIZE", "BLUE");
        put("FONT_STYLE", "CYAN");
        put("MULTIPLE", "BLACK");
    }};

    private static final List<String> COLOR_OPTIONS = Arrays.asList(
            "RED", "GREEN", "BLUE", "CYAN", "MAGENTA", "YELLOW", "BLACK", "WHITE",
            "GRAY", "DARK_GRAY", "LIGHT_GRAY", "ORANGE", "PINK", "GOLD", "BROWN", "PURPLE", "NAVY"
    );

    public static void main(String[] args) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Colors");

        // Set zoom level to 150%
        sheet.setZoom(150);

        // Create cell styles
        CellStyle headerStyle = createCellStyle(workbook, true, IndexedColors.GREY_25_PERCENT);
        CellStyle lockedStyle = createCellStyle(workbook, true, IndexedColors.LIGHT_CORNFLOWER_BLUE);
        CellStyle unlockedStyle = createCellStyle(workbook, false, IndexedColors.WHITE);
        CellStyle warningStyle = createRedFontStyle(workbook, true, IndexedColors.WHITE);

        DataValidationHelper dvHelper = sheet.getDataValidationHelper();

        // Header row (Row 0)
        Row header = sheet.createRow(0);
        Cell keyHeader = header.createCell(0);
        keyHeader.setCellValue("Key");
        keyHeader.setCellStyle(headerStyle);

        Cell colorHeader = header.createCell(1);
        colorHeader.setCellValue("Color");
        colorHeader.setCellStyle(headerStyle);

        // Row 1 → USE_DEFAULTS
        Row defaultRow = sheet.createRow(1);
        Cell defaultKey = defaultRow.createCell(0);
        defaultKey.setCellValue("USE_DEFAULTS");
        defaultKey.setCellStyle(lockedStyle);

        Cell defaultValue = defaultRow.createCell(1);
        defaultValue.setCellValue("TRUE");
        defaultValue.setCellStyle(unlockedStyle);

        // TRUE/FALSE dropdown for USE_DEFAULTS
        CellRangeAddressList tfAddress = new CellRangeAddressList(1, 1, 1, 1);
        DataValidationConstraint tfConstraint = dvHelper.createExplicitListConstraint(new String[]{"TRUE", "FALSE"});
        DataValidation tfValidation = dvHelper.createValidation(tfConstraint, tfAddress);
        tfValidation.setShowErrorBox(true);
        sheet.addValidationData(tfValidation);

        // Warning cell in column C (index 2)
        Cell warningCell = defaultRow.createCell(2);
        warningCell.setCellStyle(warningStyle);
        warningCell.setCellFormula(
                "IF(AND(TRIM(UPPER($B$2))=\"TRUE\", OR(" +
                        "TRIM(UPPER($B$4))<>\"RED\", " +
                        "TRIM(UPPER($B$5))<>\"GOLD\", " +
                        "TRIM(UPPER($B$6))<>\"MAGENTA\", " +
                        "TRIM(UPPER($B$7))<>\"BLUE\", " +
                        "TRIM(UPPER($B$8))<>\"CYAN\", " +
                        "TRIM(UPPER($B$9))<>\"BLACK\")), " +
                        "\"❗ Change USE_DEFAULTS to FALSE to apply custom colors.\", \"\")"
        );

        // Row 2 → Spacer
        sheet.createRow(2);

        // Add color config rows from Row 3 onward
        int rowIndex = 3;
        for (Map.Entry<String, String> entry : DEFAULT_COLORS.entrySet()) {
            Row row = sheet.createRow(rowIndex);

            // Key column (locked)
            Cell keyCell = row.createCell(0);
            keyCell.setCellValue(entry.getKey());
            keyCell.setCellStyle(lockedStyle);

            // Color column (unlocked)
            Cell colorCell = row.createCell(1);
            colorCell.setCellValue(entry.getValue());
            colorCell.setCellStyle(unlockedStyle);

            // Dropdown for colors
            CellRangeAddressList addressList = new CellRangeAddressList(rowIndex, rowIndex, 1, 1);
            DataValidationConstraint dvConstraint = dvHelper.createExplicitListConstraint(
                    COLOR_OPTIONS.toArray(new String[0])
            );
            DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
            validation.setShowErrorBox(true);
            sheet.addValidationData(validation);
            createRowWarningCell(row, rowIndex, sheet, warningStyle, entry.getValue());

            rowIndex++;

        }

        // Lock sheet
        sheet.protectSheet("pdfproject_password_not_guessable");

        // Auto-size columns for Key and Color
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

        // Set column width for warning (approx 60 characters)
        sheet.setColumnWidth(2, 60 * 256);

        try (FileOutputStream fos = new FileOutputStream("colors-config.xlsx")) {
            workbook.write(fos);
        }

        workbook.close();
        System.out.println("✅ colors-config.xlsx created with warning message and dropdown support.");
    }

    private static void createRowWarningCell(Row row, int rowIndex, Sheet sheet, CellStyle warningStyle, String expectedColor) {
        Cell warningCell = row.createCell(2); // Column C
        warningCell.setCellStyle(warningStyle);

        // Excel rows are 1-based (hence rowIndex + 1)
        int excelRow = rowIndex + 1;

        // Formula: show warning only if USE_DEFAULTS is TRUE and color does not match expected
        String formula = String.format(
                "IF(AND(TRIM(UPPER($B$2))=\"TRUE\", TRIM(UPPER(B%d))<>\"%s\"), \"❗ Correct color should be %s\", \"\")",
                excelRow, expectedColor.toUpperCase(), expectedColor
        );

        warningCell.setCellFormula(formula);
    }



    private static CellStyle createRedFontStyle(Workbook workbook, boolean locked, IndexedColors bgColor) {
        CellStyle style = workbook.createCellStyle();
        style.setLocked(locked);

        // Background color
        style.setFillForegroundColor(bgColor.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Borders
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        // Font
        Font font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.RED.getIndex());
        style.setFont(font);

        // Text wrap and alignment
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private static CellStyle createCellStyle(Workbook workbook, boolean locked, IndexedColors bgColor) {
        CellStyle style = workbook.createCellStyle();
        style.setLocked(locked);

        // Background color
        style.setFillForegroundColor(bgColor.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Borders
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        // Font
        Font font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);

        // Alignment
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }
}
