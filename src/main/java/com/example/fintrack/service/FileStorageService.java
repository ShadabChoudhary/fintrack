package com.example.fintrack.service;

import com.example.fintrack.dto.FileStreamResponse;
import com.example.fintrack.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;

public interface FileStorageService {
    String uploadFile(MultipartFile multipartFile, String email) throws IOException;
    FileStreamResponse downloadFile(Long expenseId, Long userId) throws AccessDeniedException;
    void deleteFile(String key);
}
