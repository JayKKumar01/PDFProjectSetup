package utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Objects;
import java.util.zip.*;

public class ExcelXMLReplacer {

    public static void replaceUserName(File xlsxFile, String src) throws IOException {
        // Step 1: Unzip to temp dir
        Path tempDir = Files.createTempDirectory("xlsx_unzip");
        unzip(xlsxFile, tempDir.toFile());

        // Step 2: Locate first sheet (xl/worksheets/sheet1.xml)
        File sheet1 = new File(tempDir.toFile(), "xl/sharedStrings.xml");
        if (!sheet1.exists()) throw new FileNotFoundException("sharedStrings.xml not found!");

        // Step 3: Read and replace
        String content = Files.readString(sheet1.toPath());
        content = content.replace(src, System.getProperty("user.name").toUpperCase());
        Files.writeString(sheet1.toPath(), content);

        // Step 4: Zip folder back into a new file
        File newFile = new File(xlsxFile.getParentFile(), "modified_" + xlsxFile.getName());
        zipDirectory(tempDir.toFile(), newFile);

        // Step 5: Replace original file
        Files.copy(newFile.toPath(), xlsxFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Replaced at: "+xlsxFile.toPath());

        // Cleanup
        deleteDirectory(tempDir);
        newFile.delete();
    }

    private static void unzip(File zipFile, File destDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File outFile = new File(destDir, entry.getName());
                if (entry.isDirectory()) {
                    outFile.mkdirs();
                } else {
                    outFile.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(outFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
            }
        }
    }

    private static void zipDirectory(File folder, File zipFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            zipFile(folder, folder, zos);
        }
    }

    private static void zipFile(File rootDir, File fileToZip, ZipOutputStream zos) throws IOException {
        if (fileToZip.isDirectory()) {
            for (File child : Objects.requireNonNull(fileToZip.listFiles())) {
                zipFile(rootDir, child, zos);
            }
        } else {
            String entryName = rootDir.toPath().relativize(fileToZip.toPath()).toString().replace("\\", "/");
            zos.putNextEntry(new ZipEntry(entryName));
            try (FileInputStream fis = new FileInputStream(fileToZip)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
            }
            zos.closeEntry();
        }
    }

    private static void deleteDirectory(Path dir) throws IOException {
        Files.walk(dir)
                .sorted((a, b) -> b.compareTo(a)) // delete children first
                .map(Path::toFile)
                .forEach(File::delete);
    }
}
