package com.example.learnhub.Service;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class ServiceOfFile implements IServiceOfFile {

    @Value("${gcp.bucket.name}")
    private String bucketName;

    @Autowired
    Storage storage;

    @Override
    public List<String> listOfFiles() {

        List<String> list = new ArrayList<>();
        Page<Blob> blobs = storage.list(bucketName);
        for (Blob blob : blobs.iterateAll()) {
            list.add(blob.getName());
        }
        return list;
    }

    @Override
    public ByteArrayResource downloadFile(String fileName) {

        Blob blob = storage.get(bucketName, fileName);
        ByteArrayResource resource = new ByteArrayResource(
                blob.getContent());

        return resource;
    }

    @Override
    public boolean deleteFile(String fileName) {

        Blob blob = storage.get(bucketName, fileName);

        return blob.delete();
    }


    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        // Loại bỏ các khoảng trắng và ký tự đặc biệt từ tên file
        String cleanedFilename = originalFilename.replaceAll("[^a-zA-Z0-9.-]", "");

        BlobId blobId = BlobId.of(bucketName, Objects.requireNonNull(cleanedFilename));
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();
        Blob blob = storage.create(blobInfo, file.getBytes());
        // Trả về đường link của tập tin đã tải lên
        return cleanedFilename;
    }

    @Override
    public String uploadVideo(MultipartFile videoFile) throws IOException {
        // Check if the uploaded file is not null and has content
        if (videoFile == null || videoFile.isEmpty()) {
            throw new IllegalArgumentException("Video file is null or empty");
        }

        // Extract the file extension from the original filename
        String originalFilename = videoFile.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("Video file has no original filename");
        }

        // Clean up the filename to remove special characters and spaces
        String cleanedFilename = originalFilename.replaceAll("[^a-zA-Z0-9.-]", "");

        // Get the file extension from the cleaned filename
        String fileExtension = cleanedFilename.substring(cleanedFilename.lastIndexOf('.') + 1);

        // Validate the file extension to ensure it's a video format you support (e.g., MP4, AVI)
        if (!isValidVideoExtension(fileExtension)) {
            throw new IllegalArgumentException("Unsupported video file format: " + fileExtension);
        }

        // Create BlobId and BlobInfo for the video file
        BlobId blobId = BlobId.of(bucketName, cleanedFilename);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(videoFile.getContentType())
                .build();

        // Upload the video file to Google Cloud Storage
        Blob blob = storage.create(blobInfo, videoFile.getBytes());

        // Return the name of the uploaded blob
        return constructFileUrl(blob.getName());
    }


    private boolean isValidVideoExtension(String extension) {
        // Add supported video file extensions here (e.g., "mp4", "avi", "mov", "wmv", etc.)
        List<String> supportedExtensions = Arrays.asList("mp4", "avi", "mov", "wmv");
        return supportedExtensions.contains(extension.toLowerCase());
    }

    @Override
    public String uploadImage(MultipartFile imageFile) throws IOException {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file is null or empty");
        }

        // Extract the file extension from the original filename
        String originalFilename = imageFile.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("Image file has no original filename");
        }

        // Clean up the filename to remove special characters and spaces
        String cleanedFilename = originalFilename.replaceAll("[^a-zA-Z0-9.-]", "");

        // Get the file extension from the cleaned filename
        String fileExtension = cleanedFilename.substring(cleanedFilename.lastIndexOf('.') + 1);

        // Validate the file extension to ensure it's an image format you support (e.g., PNG, JPEG)
        if (!isValidImageExtension(fileExtension)) {
            throw new IllegalArgumentException("Unsupported image file format: " + fileExtension);
        }

        // Create BlobId and BlobInfo for the image file
        BlobId blobId = BlobId.of(bucketName, cleanedFilename);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(imageFile.getContentType())
                .build();

        // Upload the image file to Google Cloud Storage
        Blob blob = storage.create(blobInfo, imageFile.getBytes());

        // Return the name of the uploaded blob
        return constructFileUrl(blob.getName());
    }


    private boolean isValidImageExtension(String extension) {
        // Add supported image file extensions here (e.g., "png", "jpg", "jpeg", "gif", etc.)
        List<String> supportedExtensions = Arrays.asList("png", "jpg", "jpeg", "gif");
        return supportedExtensions.contains(extension.toLowerCase());
    }

    @Override
    public String constructFileUrl(String originalFilename) {
        // Trả về URL công khai cho file
        return "https://storage.googleapis.com/" + bucketName + "/" + originalFilename;
    }
}
