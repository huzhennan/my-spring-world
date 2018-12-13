package com.example.demo.storage.s3;

import com.example.demo.storage.StorageException;
import com.example.demo.storage.StorageItem;
import com.example.demo.storage.StorageService;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Spliterator;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class S3StorageService implements StorageService {
    private static final Logger log = LoggerFactory.getLogger(S3StorageService.class);
    private MinioClient minioClient;
    private String bucketName = "images";

    @Autowired
    public S3StorageService(MinioClient minioClient) {
        this.minioClient = minioClient;

    }

    @Override
    public String generateObjectName() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public void init() {

    }

    @Override
    public Stream<URL> loadAll() {
        log.warn("================= WARNING =================");
        log.warn("Don't use this in product, just for test");
        log.warn("================= WARNING =================");
        try {
            Iterable<Result<Item>> aa = minioClient.listObjects(bucketName);
            Spliterator<Result<Item>> spliterator = aa.spliterator();
            return StreamSupport.stream(spliterator, true)
                    .map(itemResult -> {
                        try {
                            return itemResult.get();
                        } catch (Exception e) {
                            throw new StorageException("ERROR: get object item failed. ", e);
                        }
                    })
                    .map(item ->{
                        try {
                            String path = minioClient.presignedGetObject(bucketName, item.objectName());
                            return new URL(path);
                        } catch (Exception e) {
                            throw new StorageException("ERROR: get object failed. ", e);
                        }
                    });
        } catch (XmlPullParserException e) {
            throw new StorageException("ERROR: list objects failed.", e);
        }
    }

    @Override
    public StorageItem store(MultipartFile file) {
        try {
            StorageItem item = new StorageItem(generateObjectName(), file.getOriginalFilename(), file.getSize());
            minioClient.putObject(bucketName, item.getObjectName(), file.getInputStream(),
                    file.getSize(), file.getContentType());

            return item;
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException
                | XmlPullParserException e) {
            throw new StorageException("ERROR: store file to minio failed.", e);
        }
    }

    @Override
    public URL load(String objectName) {
        try {
           String path = minioClient.presignedGetObject(bucketName, objectName);

            return new URL(path);
        } catch (MinioException | NoSuchAlgorithmException | XmlPullParserException
                | IOException | InvalidKeyException e) {
            throw new StorageException("ERROR: get object failed. ", e);
        }
    }

    @Override
    public Resource loadAsResource(String objectName) {
        System.out.println("-----------------------");
        System.out.println("object name: " + objectName);
        System.out.println("-----------------------");

        URL url = load(objectName);
        return new UrlResource(url);
    }

    @Override
    public void deleteAll() {

    }
}
