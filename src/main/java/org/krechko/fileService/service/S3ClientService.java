package org.krechko.fileService.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import org.krechko.fileService.exception.FileServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ClientService {
    private final AmazonS3 s3client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public String saveFileToS3Bucket(MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                String uniqueFileKey = generateUniqueFileKey();

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                metadata.setContentType(file.getContentType());

                s3client.putObject(bucketName, uniqueFileKey, file.getInputStream(), metadata);
                return uniqueFileKey;
            } else {
                return "No file provided or file is empty";
            }
        } catch (Exception exception) {
            throw new FileServiceException("Failed to upload file", exception);
        }
    }

    private String generateUniqueFileKey() {
        try {
            String template = UUID.randomUUID().toString();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(template.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new FileServiceException("NoSuchAlgorithmException while generating key", e);
        }
    }

    public byte[] getFile(String uniqueFileKey) {
        try {
            S3Object s3Object = s3client.getObject(bucketName, uniqueFileKey);
            try (S3ObjectInputStream inputStream = s3Object.getObjectContent()) {
                byte[] bytes = inputStream.readAllBytes();
                return bytes;
            }
        } catch (Exception exception) {
            throw new FileServiceException("Failed to get file from S3", exception);
        }
    }
}
