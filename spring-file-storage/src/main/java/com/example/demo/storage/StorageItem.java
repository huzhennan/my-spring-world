package com.example.demo.storage;

public class StorageItem {
    private String objectName;
    private String originalFilename;
    private long size;

    public StorageItem() {
    }

    public StorageItem(String objectName, String originalFilename, long size) {
        this.objectName = objectName;
        this.originalFilename = originalFilename;
        this.size = size;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "StorageItem{" +
                "objectName='" + objectName + '\'' +
                ", originalFilename='" + originalFilename + '\'' +
                ", size=" + size +
                '}';
    }
}
