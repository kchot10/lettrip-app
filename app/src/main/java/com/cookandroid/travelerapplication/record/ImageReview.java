package com.cookandroid.travelerapplication.record;

public class ImageReview {

    String imageUrl;
    String fileSize;
    String originalFileName;
    String storedFileName;

    public ImageReview() {
    }

    public ImageReview(String imageUrl, String fileSize, String originalFileName, String storedFileName) {
        this.imageUrl = imageUrl;
        this.fileSize = fileSize;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }
}
