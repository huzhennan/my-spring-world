package com.example.demo;

import com.example.demo.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

//import javax.annotation.Resource;

//@Controller
public class HelloController {
    @Autowired
    private StorageService storageService;


    @GetMapping("/{objectName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFoo(@PathVariable String objectName) {

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/file");

        Resource resource = storageService.loadAsResource(objectName);

        return ResponseEntity.ok().headers(responseHeaders).body(resource);
    }
}
