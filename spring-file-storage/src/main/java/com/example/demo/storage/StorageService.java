package com.example.demo.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.stream.Stream;

public interface StorageService {
    String generateObjectName();

    void init();

    StorageItem store(MultipartFile file);

    Stream<URL> loadAll();

    URL load(String objectName);

    Resource loadAsResource(String objectName);

    void deleteAll();
}
