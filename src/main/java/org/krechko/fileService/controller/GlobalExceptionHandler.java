package org.krechko.fileService.controller;

import org.krechko.fileService.exception.ErrorResponse;
import org.krechko.fileService.exception.FileServiceIOException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FileServiceIOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleFileServiceIOException(FileServiceIOException exception) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            errorResponse.setMessage(exception.getMessage());
            errorResponse.setTimestamp(LocalDateTime.now());
        return errorResponse;
    }
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ErrorResponse handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
            errorResponse.setError(HttpStatus.PAYLOAD_TOO_LARGE.getReasonPhrase());
            errorResponse.setMessage("File size exceeds the allowable limit!");
            errorResponse.setTimestamp(LocalDateTime.now());
        return errorResponse;
    }
}