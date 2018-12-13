package com.example.demo.storage.file;

import com.example.demo.FileUploadController;
import com.example.demo.storage.StorageException;
import com.example.demo.storage.StorageItem;
import com.example.demo.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public StorageItem store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                StorageItem item = new StorageItem(generateObjectName(), file.getOriginalFilename(), file.getSize());
                Files.copy(inputStream, this.rootLocation.resolve(item.getObjectName()),
                    StandardCopyOption.REPLACE_EXISTING);

                return item;
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public Stream<URL> loadAll() {

        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                            "serveFile", path.getFileName().toString()).build().toString())
                    .map(path -> {
                        try {
                            return new URL(path);
                        } catch (MalformedURLException e) {
                            throw new StorageException("Failed to generate file's url", e);
                        }
                    });
        } catch (IOException e) {
                throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public URL load(String objectName) {
        try {
            return rootLocation.resolve(objectName).toUri().toURL();
        } catch (MalformedURLException e) {
            throw new StorageException("Failed to generate url for file", e);
        }
    }

    @Override
    public Resource loadAsResource(String objectName) {
        URL file = load(objectName);
        Resource resource = new UrlResource(file);
        if (resource.exists() || resource.isReadable()) {
            return resource;
        }
        else {
            throw new StorageFileNotFoundException(
                    "Could not read file: " + objectName);

        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public String generateObjectName() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
