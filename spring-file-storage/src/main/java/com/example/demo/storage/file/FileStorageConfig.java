package com.example.demo.storage.file;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("file-storage")
@EnableConfigurationProperties(StorageProperties.class)
public class FileStorageConfig {

    @Bean
    public FileSystemStorageService fileSystemStorageService(StorageProperties storageProperties) {
        return new FileSystemStorageService(storageProperties);
    }
}
