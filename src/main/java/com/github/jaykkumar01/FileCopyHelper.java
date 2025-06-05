import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class FileCopyHelper {

    public static void main(String[] args) throws IOException {
        // === Base directory where the JAR is located ===
        Path baseDir = new File(".").getCanonicalFile().toPath();

        // === Arrays of folders and their destination folders ===
        String[] folderNames = { "FolderA", "FolderB" };
        String[] folderDestinations = { "destination/folder1", "destination/folder2" };

        // === Arrays of file names and their destination folders ===
        String[] fileNames = { "extraFile.txt", "config.properties" };
        String[] fileDestinations = { "destination/singlefile", "destination/configs" };

        // === Build folder and file maps ===
        Map<Path, Path> foldersToCopy = buildFolderMap(baseDir, folderNames, folderDestinations);
        Map<Path, Path> filesToCopy = buildFileMap(baseDir, fileNames, fileDestinations);

        // === Copy folders ===
        for (Map.Entry<Path, Path> entry : foldersToCopy.entrySet()) {
            copyDirectory(entry.getKey(), entry.getValue());
        }

        // === Copy files ===
        for (Map.Entry<Path, Path> entry : filesToCopy.entrySet()) {
            copySingleFileToFolder(entry.getKey(), entry.getValue());
        }

        System.out.println("All folders and files copied successfully.");
    }

    // === Utility: Build folder map from names ===
    private static Map<Path, Path> buildFolderMap(Path baseDir, String[] sourceNames, String[] destinationFolders) {
        Map<Path, Path> map = new LinkedHashMap<>();
        for (int i = 0; i < sourceNames.length; i++) {
            map.put(baseDir.resolve(sourceNames[i]), Paths.get(destinationFolders[i]));
        }
        return map;
    }

    // === Utility: Build file map from names ===
    private static Map<Path, Path> buildFileMap(Path baseDir, String[] fileNames, String[] destinationFolders) {
        Map<Path, Path> map = new LinkedHashMap<>();
        for (int i = 0; i < fileNames.length; i++) {
            map.put(baseDir.resolve(fileNames[i]), Paths.get(destinationFolders[i]));
        }
        return map;
    }

    private static void copyDirectory(Path source, Path target) throws IOException {
        if (!Files.exists(source)) {
            System.err.println("Folder not found: " + source);
            return;
        }

        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path destDir = target.resolve(source.relativize(dir));
                Files.createDirectories(destDir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path destFile = target.resolve(source.relativize(file));
                Files.copy(file, destFile, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void copySingleFileToFolder(Path sourceFile, Path targetFolder) throws IOException {
        if (!Files.exists(sourceFile)) {
            System.err.println("File not found: " + sourceFile);
            return;
        }

        Files.createDirectories(targetFolder);
        Path targetFile = targetFolder.resolve(sourceFile.getFileName());
        Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
    }
}
