package org.krechko.fileService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {
    private final S3ClientService s3ClientService;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public String uploadFile(MultipartFile file) {
        String uniqueFileKey = s3ClientService.saveFileToS3Bucket(file);
      //  kafkaTemplate.send("song-to-save-topic", uniqueFileKey);
        return uniqueFileKey;
    }

    public byte[] getFileFromS3(String uniqueFileKey) {
        return s3ClientService.getFile(uniqueFileKey);
    }
}
