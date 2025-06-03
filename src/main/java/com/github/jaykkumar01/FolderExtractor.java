package com.github.jaykkumar01;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FolderExtractor {

    public interface CopyListener {
        void onProgress(int current, int total);
        void onLog(String message);
    }

    private static final String[] RESOURCE_FOLDERS = {"projects", "output", "commands"};
    private static final String userDir = System.getProperty("user.home");

    private static final String[] DESTINATION_FOLDERS = {
            userDir + "/.epsilon/Projects",
            userDir + "/Downloads",
            userDir + "/.epsilon/lib/commands"
    };

    public void extractAll(CopyListener listener) {
        try {
            List<Path> allFiles = countAllFiles();
            int total = allFiles.size();
            int copied = 0;

            File jarDir = new File(".").getCanonicalFile();

            for (int i = 0; i < RESOURCE_FOLDERS.length; i++) {
                Path sourceFolder = jarDir.toPath().resolve(RESOURCE_FOLDERS[i]);
                Path destinationFolder = Paths.get(DESTINATION_FOLDERS[i]);
                copied += copyFolder(sourceFolder, destinationFolder, listener, copied, total);
            }

            listener.onLog("✅ Extraction complete!");

        } catch (Exception e) {
            listener.onLog("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int copyFolder(Path sourceFolder, Path destinationFolder, CopyListener listener,
                           int copiedSoFar, int totalFiles) throws IOException {

        AtomicInteger copied = new AtomicInteger();

        if (!Files.exists(sourceFolder)) {
            listener.onLog("⚠️ Folder not found: " + sourceFolder);
            return 0;
        }

        Files.walkFileTree(sourceFolder, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path relative = sourceFolder.relativize(file);
                Path dest = destinationFolder.resolve(relative);
                Files.createDirectories(dest.getParent());
                Files.copy(file, dest, StandardCopyOption.REPLACE_EXISTING);
                listener.onLog("📁 " + file + " → " + dest);
                listener.onProgress(copied.incrementAndGet() + copiedSoFar, totalFiles);
                return FileVisitResult.CONTINUE;
            }
        });

        return copied.get();
    }

    private List<Path> countAllFiles() throws IOException {
        List<Path> files = new ArrayList<>();
        File jarDir = new File(".").getCanonicalFile();

        for (String folderName : RESOURCE_FOLDERS) {
            Path folder = jarDir.toPath().resolve(folderName);
            if (!Files.exists(folder)) continue;

            Files.walk(folder)
                    .filter(Files::isRegularFile)
                    .forEach(files::add);
        }

        return files;
    }
}
