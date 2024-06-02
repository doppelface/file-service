package org.krechko.fileService.controller;

import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import org.krechko.fileService.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FileController {
    private final FileService fileService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadFile(@RequestParam("file") MultipartFile file) {
        fileService.uploadFile(file);
    }
    @GetMapping("/{key}")
    @ResponseStatus(HttpStatus.OK)
    public byte[] getFileFromS3ByKey(@PathVariable("key") String key) {
        return fileService.getFileByKey(key);
    }
}
