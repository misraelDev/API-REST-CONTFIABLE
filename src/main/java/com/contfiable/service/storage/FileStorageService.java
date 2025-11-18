package com.contfiable.service.storage;

import com.contfiable.exception.BadRequestException;
import com.contfiable.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new BusinessException("No se pudo crear el directorio de almacenamiento de archivos.", ex);
        }
    }

    public String storeFile(MultipartFile file, String subFolder) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.contains("..")) {
            throw new BadRequestException("Nombre de archivo inválido: " + originalFilename);
        }

        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }

        String fileName = UUID.randomUUID().toString() + extension;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(subFolder).resolve(fileName);
            Files.createDirectories(targetLocation.getParent());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return "/" + subFolder + "/" + fileName;
        } catch (IOException ex) {
            throw new BusinessException("No se pudo almacenar el archivo " + fileName, ex);
        }
    }

    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }
        try {
            Path filePath = this.fileStorageLocation.resolve(fileUrl.substring(1)).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            // Log error but don't fail the operation
        }
    }
}
