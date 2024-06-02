package org.krechko.fileService.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import org.krechko.fileService.exception.FileServiceIOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ClientService {
    private final AmazonS3 s3client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public String saveFileToS3Bucket(MultipartFile file) {
        try {
            String uniqueFileKey = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            s3client.putObject(bucketName, uniqueFileKey, file.getInputStream(), metadata);
            return uniqueFileKey;
        } catch (Exception exception) {
            throw new FileServiceIOException("Failed to upload file", exception);

        }
    }

    public byte[] getFileFromS3Bucket(String uniqueFileKey) {
        try {
            S3Object s3Object = s3client.getObject(bucketName, uniqueFileKey);
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
                byte[] bytes = inputStream.readAllBytes();
            return bytes;
        } catch (Exception exception) {
            throw new FileServiceIOException("Failed to get file from S3", exception);
        }
    }
}
