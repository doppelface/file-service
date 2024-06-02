package org.krechko.fileService.exception;

public class FileServiceIOException extends RuntimeException {
    public FileServiceIOException(String message, Throwable cause) {
        super(message, cause);
    }
}