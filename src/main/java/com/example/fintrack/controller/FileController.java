package com.example.fintrack.controller;

import com.example.fintrack.dto.FileStreamResponse;
import com.example.fintrack.model.User;
import com.example.fintrack.service.FileStorageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;


@RestController
@RequestMapping("/file")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService){
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/download/expense/{id}")
    public ResponseEntity<InputStreamResource> download(@PathVariable Long id, @AuthenticationPrincipal User user) throws AccessDeniedException {
        FileStreamResponse streamResponse = fileStorageService.downloadFile(id, user.getId());
        InputStreamResource resource = new InputStreamResource(streamResponse.getInputStream());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + streamResponse.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(streamResponse.getContentType()))
                .contentLength(streamResponse.getContentLength())
                .body(resource);
    }
}
