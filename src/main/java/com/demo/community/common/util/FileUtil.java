package com.demo.community.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
public class FileUtil {

    private FileUtil() {
    }

    public static String uploadFile(String rootDir, String dir, MultipartFile file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File is null.");
        }

        try (InputStream inputStream = file.getInputStream()) {
            String filePath = getAbsoluteFilePath(rootDir, dir, file.getOriginalFilename());
            Files.copy(inputStream, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            return filePath;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    public static void deleteFile(String filePath, String fileName) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                log.error("Failed to delete the file: {}", filePath);
                throw new IOException("Failed to delete the file: " + fileName);
            }
        }
    }

    public static Resource loadAsResource(String filePath) throws IOException {
        try {
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found.");
            }
        } catch (MalformedURLException e) {
            log.error("Invalid URL format: {}", filePath, e);
            throw new MalformedURLException("Invalid URL format.");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    public static String getAbsoluteFilePath(String rootDir, String dir, String fileName) throws IOException {
        return getOrCreateDir(rootDir, dir) + File.separator + fileName;
    }

    public static String getOrCreateDir(String rootDir, String dir) throws IOException {
        String uploadDir = rootDir + File.separator + dir;
        Path uploadDirPath = Paths.get(uploadDir);
        if (!Files.exists(uploadDirPath)) {
            Files.createDirectories(uploadDirPath);
        }
        return uploadDir;
    }

}
