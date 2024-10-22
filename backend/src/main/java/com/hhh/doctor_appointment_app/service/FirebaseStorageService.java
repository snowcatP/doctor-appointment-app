package com.hhh.doctor_appointment_app.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import com.google.cloud.storage.Blob;
import java.util.UUID;

@Service
public class FirebaseStorageService {
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Bucket bucket = StorageClient.getInstance().bucket();

        // Tạo BlobInfo với các thuộc tính cần thiết
        BlobInfo blobInfo = BlobInfo.newBuilder(bucket.getName(), fileName)
                .setContentType(file.getContentType())
                .build();

        // Upload tệp lên Firebase Storage và tạo blob
        bucket.getStorage().create(blobInfo, file.getBytes());

        // Tạo URL truy cập công khai cho tệp đã tải lên
        String fileUrl = String.format(
                "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucket.getName(),
                java.net.URLEncoder.encode(fileName, "UTF-8")
        );

        return fileUrl;
    }
}
