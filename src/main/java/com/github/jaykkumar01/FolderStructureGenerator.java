package com.github.jaykkumar01;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class FolderStructureGenerator {

    private static final String BASE_PATH = "src/main/resources";
    private static final String[] BASE_FOLDERS = {"projects", "output", "commands"};

    private static final String[] FILE_NAMES = {
        "Readme", "Main", "Config", "Setup", "init", "App", "log", "notes",
        ".env", ".gitignore", ".dockerignore", ".npmrc", "data", "result", "index"
    };

    private static final String[] EXTENSIONS = {
        ".txt", ".md", ".json", ".java", ".png", ".log", ".csv", ".xml", "", ".yml"
    };

    private static final String[] FOLDER_NAMES = {
        "Docs", "bin", "src", "lib", "assets", "Static", "dist", "Test", "tmp", "resources",
        ".cache", ".settings", ".vscode", ".idea", "config"
    };

    private static final int MAX_DEPTH = 4;
    private static final int MAX_FILES_PER_FOLDER = 6;
    private static final int MAX_SUBFOLDERS = 4;

    private static final Random random = new Random();

    public static void main(String[] args) {
        for (String folder : BASE_FOLDERS) {
            File subFolder = new File(BASE_PATH, folder);
            try {
                createFolderRecursive(subFolder, 0);
            } catch (IOException e) {
                System.err.println("Failed to create structure in: " + subFolder.getPath());
                e.printStackTrace();
            }
        }
        System.out.println("📁 Random folder/file structure created in src/main/resources.");
    }

    private static void createFolderRecursive(File folder, int depth) throws IOException {
        if (depth > MAX_DEPTH) return;

        if (!folder.exists()) folder.mkdirs();

        // Create random files
        int fileCount = random.nextInt(MAX_FILES_PER_FOLDER + 1);
        for (int i = 0; i < fileCount; i++) {
            String fileName = getRandomFileName();
            File file = new File(folder, fileName);
            if (file.createNewFile()) {
                try (FileWriter fw = new FileWriter(file)) {
                    fw.write("Sample content for " + fileName);
                }
            }
        }

        // Create random subfolders
        int subFolderCount = random.nextInt(MAX_SUBFOLDERS + 1);
        for (int i = 0; i < subFolderCount; i++) {
            File subFolder = new File(folder, getRandomFolderName());
            createFolderRecursive(subFolder, depth + 1);
        }
    }

    private static String getRandomFileName() {
        String name = FILE_NAMES[random.nextInt(FILE_NAMES.length)];
        String ext = EXTENSIONS[random.nextInt(EXTENSIONS.length)];
        return name + ext;
    }

    private static String getRandomFolderName() {
        return FOLDER_NAMES[random.nextInt(FOLDER_NAMES.length)];
    }
}
