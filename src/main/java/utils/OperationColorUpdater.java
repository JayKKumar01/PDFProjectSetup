package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class OperationColorUpdater {

    private static final Map<String, Color> COLOR_NAME_MAP = new LinkedHashMap<>() {{
        put("RED", Color.RED);
        put("GREEN", Color.GREEN);
        put("BLUE", Color.BLUE);
        put("CYAN", Color.CYAN);
        put("MAGENTA", Color.MAGENTA);
        put("YELLOW", Color.YELLOW);
        put("BLACK", Color.BLACK);
        put("WHITE", Color.WHITE);
        put("GRAY", Color.GRAY);
        put("DARK_GRAY", Color.DARK_GRAY);
        put("LIGHT_GRAY", Color.LIGHT_GRAY);
        put("ORANGE", Color.ORANGE);
        put("PINK", Color.PINK);
        put("BROWN", new Color(150, 75, 0));
        put("PURPLE", new Color(128, 0, 128));
        put("NAVY", new Color(0, 0, 128));
        put("GOLD", new Color(255, 215, 0));
    }};

    public static void updateColorsFromExcel(String excelPath) {
        try (FileInputStream fis = new FileInputStream(excelPath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("Colors");
            if (sheet == null) {
                System.err.println("❌ Sheet 'Colors' not found.");
                return;
            }

            boolean useDefaults = true;
            Row useDefaultsRow = sheet.getRow(1);
            if (useDefaultsRow != null) {
                Cell cell = useDefaultsRow.getCell(1);
                if (cell != null) {
                    if (cell.getCellType() == CellType.BOOLEAN) {
                        useDefaults = cell.getBooleanCellValue();
                    } else if (cell.getCellType() == CellType.STRING) {
                        useDefaults = "TRUE".equalsIgnoreCase(cell.getStringCellValue().trim());
                    }
                }
            }


            if (useDefaults) {
                System.out.println("✅ USE_DEFAULTS is TRUE. No custom colors applied.");
                return;
            }

            for (int i = 3; i <= 8; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell keyCell = row.getCell(0);
                Cell colorCell = row.getCell(1);

                if (keyCell == null || colorCell == null) continue;

                String key = keyCell.getStringCellValue().trim().toUpperCase(Locale.ROOT);
                String colorName = colorCell.getStringCellValue().trim().toUpperCase(Locale.ROOT);

                Color newColor = COLOR_NAME_MAP.get(colorName);
                if (newColor == null) {
                    System.err.printf("⚠️ Unknown color: %s for key %s%n", colorName, key);
                    continue;
                }

                switch (key) {
                    case "DELETED" -> OperationColor.DELETED = newColor;
                    case "ADDED" -> OperationColor.ADDED = newColor;
                    case "FONT_NAME" -> OperationColor.FONT_NAME = newColor;
                    case "FONT_SIZE" -> OperationColor.FONT_SIZE = newColor;
                    case "FONT_STYLE" -> OperationColor.FONT_STYLE = newColor;
                    case "MULTIPLE" -> OperationColor.MULTIPLE = newColor;
                    default -> System.err.printf("⚠️ Unknown key: %s%n", key);
                }

                System.out.printf("✅ %s set to %s%n", key, colorName);
            }

        } catch (Exception e) {
            System.err.println("❌ Failed to read color config: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void printOperationColors() {
        Map<String, Color> currentValues = new LinkedHashMap<>();
        currentValues.put("DELETED", OperationColor.DELETED);
        currentValues.put("ADDED", OperationColor.ADDED);
        currentValues.put("FONT_NAME", OperationColor.FONT_NAME);
        currentValues.put("FONT_SIZE", OperationColor.FONT_SIZE);
        currentValues.put("FONT_STYLE", OperationColor.FONT_STYLE);
        currentValues.put("MULTIPLE", OperationColor.MULTIPLE);

        // Reverse lookup to get human-readable name
        Map<Color, String> reverseColorMap = new HashMap<>();
        for (Map.Entry<String, Color> entry : COLOR_NAME_MAP.entrySet()) {
            reverseColorMap.put(entry.getValue(), entry.getKey());
        }

        System.out.println("\n🎨 OperationColor values (as readable names):");
        for (Map.Entry<String, Color> entry : currentValues.entrySet()) {
            String name = entry.getKey();
            Color color = entry.getValue();
            String colorName = reverseColorMap.getOrDefault(color, String.format("RGB(%d,%d,%d)", color.getRed(), color.getGreen(), color.getBlue()));
            System.out.printf("%-12s = %s%n", name, colorName);
        }
    }

    public static void main(String[] args) {
        String excelPath = "colors-config.xlsx";
        updateColorsFromExcel(excelPath);
        printOperationColors();
    }
}
