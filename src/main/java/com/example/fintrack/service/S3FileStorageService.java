package com.example.fintrack.service;

import com.example.fintrack.dto.FileStreamResponse;
import com.example.fintrack.exception.ExpenseNotFoundException;
import com.example.fintrack.exception.ResourceNotFoundException;
import com.example.fintrack.model.Expense;
import com.example.fintrack.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.util.UUID;

@Service
public class S3FileStorageService implements FileStorageService{

    private final S3Client s3Client;
    private final ExpenseRepository expenseRepository;
    @Value("${aws.bucket.name}")
    private String bucketName;

    public S3FileStorageService(S3Client s3Client, ExpenseRepository expenseRepository){
        this.s3Client = s3Client;
        this.expenseRepository = expenseRepository;
    }

    public String uploadFile(MultipartFile file, String email) throws IOException {
        String key = email + "/receipts" + UUID.randomUUID() + "-"+ file.getOriginalFilename();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(file.getContentType())
                        .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        return key;
    }

    public FileStreamResponse downloadFile(Long expenseId, Long userId) throws AccessDeniedException {
        Expense getExpense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException("No Expense found with this ID"));

        if(!getExpense.getUser().getId().equals(userId)){
            throw new AccessDeniedException("Access Denied: You do not own this expense receipt.");
        }
        String key = getExpense.getReceiptKey();
        if (key == null || key.isEmpty()) {
            throw new ResourceNotFoundException("No receipt file is linked to this expense.");
        }
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try{
            ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(getObjectRequest);
            String originalFileName = key.substring(key.lastIndexOf('-') + 1);

            return new FileStreamResponse(
                    responseInputStream,
                    originalFileName,
                    responseInputStream.response().contentType(),
                    responseInputStream.response().contentLength()
            );

        }catch(S3Exception e){
            throw new RuntimeException("Error downloading file: "+e.awsErrorDetails().errorMessage(), e);
        }catch (Exception e) {
            // Final catch-all for network or unexpected I/O errors
            throw new RuntimeException("Unexpected error during S3 receipt download.", e);
        }
    }

    @Override
    public void deleteFile(String key) {

    }

}
