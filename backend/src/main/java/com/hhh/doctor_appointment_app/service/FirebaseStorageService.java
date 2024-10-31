package com.hhh.doctor_appointment_app.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import com.google.cloud.storage.Blob;

import java.io.InputStream;
import java.util.UUID;

@Service
public class FirebaseStorageService {
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB in bytes

    public String uploadFile(MultipartFile file) throws IOException {
        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IOException("File size exceeds the 2MB limit.");
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Bucket bucket = StorageClient.getInstance().bucket();

        // Create BlobInfo with required properties
        BlobInfo blobInfo = BlobInfo.newBuilder(bucket.getName(), fileName)
                .setContentType(file.getContentType())
                .build();

        // Using InputStream to upload files to Firebase Storage
        try (InputStream inputStream = file.getInputStream()) {
            bucket.getStorage().create(blobInfo, inputStream);
        }

        // Generate a publicly accessible URL for the uploaded file
        String fileUrl = String.format(
                "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucket.getName(),
                java.net.URLEncoder.encode(fileName, "UTF-8")
        );

        return fileUrl;
    }
}

