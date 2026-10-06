package com.papaya.notice.util;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Component
public class FileStorageUtil {

    //  Folder where files will be saved
    private final Path uploadDir = Paths.get("uploads");

    public String saveFile(MultipartFile file) {
        try {

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir); // 👈 auto-create
            }

            // Step 2: Clean and prepare filename
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileName = System.currentTimeMillis() + "_" + originalFilename;

            // Step 3: Resolve the full path
            Path targetLocation = uploadDir.resolve(fileName);

            //  Step 4: Copy file to location
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
        }
    }
}
