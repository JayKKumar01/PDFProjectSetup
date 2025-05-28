package com.github.jaykkumar01;

import java.io.*;
import java.nio.file.*;

public class ResourceFolderCopier {

    private static final String RESOURCE_BASE = "src/main/resources";
    private static final String[] SOURCE_FOLDERS = {"project", "images", "commands"};
    private static final String[] DESTINATION_BASES = {
        "destination/base1",
        "destination/base2",
        "destination/base3"
    };

    public static void main(String[] args) {
        for (int i = 0; i < SOURCE_FOLDERS.length; i++) {
            File source = new File(RESOURCE_BASE, SOURCE_FOLDERS[i]);
            File destination = new File(DESTINATION_BASES[i]);

            try {
                copyFolder(source.toPath(), destination.toPath());
                System.out.println("✅ Copied " + source + " → " + destination);
            } catch (IOException e) {
                System.err.println("❌ Failed to copy " + source + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void copyFolder(Path source, Path destination) throws IOException {
        if (!Files.exists(source)) {
            System.out.println("⚠️ Source folder doesn't exist: " + source);
            return;
        }

        Files.walk(source).forEach(path -> {
            try {
                Path relativePath = source.relativize(path);
                Path targetPath = destination.resolve(relativePath);

                if (Files.isDirectory(path)) {
                    if (!Files.exists(targetPath)) {
                        Files.createDirectories(targetPath);
                    }
                } else {
                    Files.copy(path, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new RuntimeException("Error copying: " + path + " → " + destination, e);
            }
        });
    }
}
