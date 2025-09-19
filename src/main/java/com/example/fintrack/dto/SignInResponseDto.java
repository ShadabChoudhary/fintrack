package com.example.fintrack.dto;

import com.example.fintrack.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInResponseDto {
    private long userId;
    private String message;
    private String token;
}
