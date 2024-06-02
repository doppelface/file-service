package org.krechko.fileService.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import org.krechko.fileService.exception.FileServiceIOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final AmazonS3 s3client;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public void uploadFile(MultipartFile file) {
        try {
            String uniqueFileKey = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            s3client.putObject(bucketName, uniqueFileKey, file.getInputStream(), metadata);

            kafkaTemplate.send("song-to-save-topic", uniqueFileKey);
        } catch (Exception exception) {
            throw new FileServiceIOException("Failed to upload file", exception);

        }
    }

    public byte[] getFileByKey(String key) {
        try {
            S3Object s3Object = s3client.getObject(bucketName, key);
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
                byte[] bytes = inputStream.readAllBytes();
            return bytes;
        } catch (Exception exception) {
            throw new FileServiceIOException("Failed to get file from S3", exception);
        }





    }

}
