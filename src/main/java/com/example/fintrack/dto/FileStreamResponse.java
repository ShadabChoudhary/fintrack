package com.example.fintrack.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.InputStream;

@Getter
@Setter
@RequiredArgsConstructor
public class FileStreamResponse {
    private final InputStream inputStream;
    private final String fileName;
    private final String contentType;
    private final long contentLength;
}
