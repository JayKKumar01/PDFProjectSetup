package com.github.jaykkumar01;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ResourceFolderExtractor {

    private static final String[] RESOURCE_FOLDERS = {"project", "images", "commands"};
    private static final String[] DESTINATION_FOLDERS = {
        "destination/"+RESOURCE_FOLDERS[0],
        "destination/"+RESOURCE_FOLDERS[1],
        "destination/"+RESOURCE_FOLDERS[2]
    };

    public static void main(String[] args) {
        for (int i = 0; i < RESOURCE_FOLDERS.length; i++) {
            try {
                extractResourceFolder(RESOURCE_FOLDERS[i], DESTINATION_FOLDERS[i]);
                System.out.println("✅ Extracted " + RESOURCE_FOLDERS[i] + " → " + DESTINATION_FOLDERS[i]);
            } catch (Exception e) {
                System.err.println("❌ Failed to extract " + RESOURCE_FOLDERS[i] + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void extractResourceFolder(String resourceFolder, String destinationFolder) throws IOException, URISyntaxException {
        URL resourceUrl = ResourceFolderExtractor.class.getClassLoader().getResource(resourceFolder);
        if (resourceUrl == null) {
            System.err.println("⚠️ Resource folder not found: " + resourceFolder);
            return;
        }

        if (resourceUrl.getProtocol().equals("jar")) {
            // Inside a JAR
            String jarPath = resourceUrl.getPath().substring(5, resourceUrl.getPath().indexOf("!"));
            jarPath = URLDecoder.decode(jarPath, "UTF-8");

            try (JarFile jarFile = new JarFile(jarPath)) {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();

                    if (name.startsWith(resourceFolder + "/") && !entry.isDirectory()) {
                        InputStream is = jarFile.getInputStream(entry);
                        Path destPath = Paths.get(destinationFolder, name.substring(resourceFolder.length() + 1));
                        Files.createDirectories(destPath.getParent());
                        Files.copy(is, destPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        } else if (resourceUrl.getProtocol().equals("file")) {
            // Development mode (not in JAR)
            Path sourcePath = Paths.get(resourceUrl.toURI());
            Files.walk(sourcePath).forEach(path -> {
                try {
                    if (Files.isRegularFile(path)) {
                        Path relative = sourcePath.relativize(path);
                        Path dest = Paths.get(destinationFolder, relative.toString());
                        Files.createDirectories(dest.getParent());
                        Files.copy(path, dest, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to copy file: " + path, e);
                }
            });
        }
    }
}
