package com.github.jaykkumar01;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.*;

public class ResourceFolderExtractor {

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
            List<String> allFiles = countAllFiles();
            int total = allFiles.size();
            int copied = 0;

            for (int i = 0; i < RESOURCE_FOLDERS.length; i++) {
                System.out.println(DESTINATION_FOLDERS[i]);
                copied += extractResourceFolder(RESOURCE_FOLDERS[i], DESTINATION_FOLDERS[i], listener, copied, total);
            }

            listener.onLog("✅ Extraction complete!");

        } catch (Exception e) {
            listener.onLog("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int extractResourceFolder(String resourceFolder, String destinationFolder, CopyListener listener,
                                      int copiedSoFar, int totalFiles)
            throws IOException, URISyntaxException {

        AtomicInteger copied = new AtomicInteger();
        URL resourceUrl = getClass().getClassLoader().getResource(resourceFolder);
        if (resourceUrl == null) {
            listener.onLog("⚠️ Resource not found: " + resourceFolder);
            return 0;
        }

        if ("jar".equals(resourceUrl.getProtocol())) {
            String jarPath = resourceUrl.getPath().substring(5, resourceUrl.getPath().indexOf("!"));
            jarPath = URLDecoder.decode(jarPath, "UTF-8");

            try (JarFile jar = new JarFile(jarPath)) {
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();

                    if (name.startsWith(resourceFolder + "/") && !entry.isDirectory()) {
                        InputStream is = jar.getInputStream(entry);
                        Path dest = Paths.get(destinationFolder, name.substring(resourceFolder.length() + 1));
                        Files.createDirectories(dest.getParent());
                        Files.copy(is, dest, StandardCopyOption.REPLACE_EXISTING);
                        listener.onLog("📁 " + name + " → " + dest);
                        listener.onProgress(copied.incrementAndGet() + copiedSoFar, totalFiles);
                    }
                }
            }
        } else if ("file".equals(resourceUrl.getProtocol())) {
            Path sourcePath = Paths.get(resourceUrl.toURI());
            Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path relative = sourcePath.relativize(file);
                    Path dest = Paths.get(destinationFolder, relative.toString());
                    Files.createDirectories(dest.getParent());
                    Files.copy(file, dest, StandardCopyOption.REPLACE_EXISTING);
                    listener.onLog("📁 " + relative + " → " + dest);
                    listener.onProgress(copied.incrementAndGet() + copiedSoFar, totalFiles);
                    return FileVisitResult.CONTINUE;
                }
            });

        }

        return copied.get();
    }

    private List<String> countAllFiles() throws IOException, URISyntaxException {
        List<String> files = new ArrayList<>();

        for (String folder : RESOURCE_FOLDERS) {
            URL resourceUrl = getClass().getClassLoader().getResource(folder);
            if (resourceUrl == null) continue;

            if ("jar".equals(resourceUrl.getProtocol())) {
                String jarPath = resourceUrl.getPath().substring(5, resourceUrl.getPath().indexOf("!"));
                jarPath = URLDecoder.decode(jarPath, "UTF-8");

                try (JarFile jar = new JarFile(jarPath)) {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        if (entry.getName().startsWith(folder + "/") && !entry.isDirectory()) {
                            files.add(entry.getName());
                        }
                    }
                }
            } else if ("file".equals(resourceUrl.getProtocol())) {
                Path path = Paths.get(resourceUrl.toURI());
                Files.walk(path)
                        .filter(Files::isRegularFile)
                        .forEach(p -> files.add(p.toString()));
            }
        }
        return files;
    }
}
