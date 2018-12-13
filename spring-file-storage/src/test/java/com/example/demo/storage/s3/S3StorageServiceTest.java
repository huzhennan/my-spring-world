package com.example.demo.storage.s3;

import com.example.demo.storage.StorageItem;
import com.example.demo.storage.StorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class S3StorageServiceTest {
    @Autowired
    private StorageService storageService;

    @Test
    public void testStore() {
        MockMultipartFile multipartFile = new MockMultipartFile("test-name.txt",
                "test-original-file-name.txt", "text/plain", "Spring Framework".getBytes());

        StorageItem storageItem = storageService.store(multipartFile);
        assertNotNull(storageItem);
    }

    @Test
    public void testLoadAsResource() {
        Resource resource = storageService.loadAsResource("test-original-file-name.txt");
        System.out.println("------------------------");
        System.out.println(resource.getDescription());
        System.out.println("------------------------");
    }

    @Test
    public void testLoadAll() {
        System.out.println("--------------------------");
        Stream<URL> urlStream = storageService.loadAll();
        urlStream.forEach(System.out::println);
        System.out.println("--------------------------");
    }
}
