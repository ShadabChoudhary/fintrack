package com.example.fintrack.dto;

import com.example.fintrack.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {
    private String name;
    private String email;
    private String password;
}
