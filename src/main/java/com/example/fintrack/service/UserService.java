package com.example.fintrack.service;


import com.example.fintrack.dto.SignInResponseDto;
import com.example.fintrack.dto.SignUpResponseDto;
import com.example.fintrack.exception.InvalidCredentialsException;
import com.example.fintrack.exception.UserAlreadyExistException;
import com.example.fintrack.exception.UserNotFoundException;
import com.example.fintrack.model.User;

public interface UserService {
    SignUpResponseDto signUp(String name, String email, String password) throws UserAlreadyExistException;
    SignInResponseDto singIn(String email, String password)throws UserNotFoundException, InvalidCredentialsException;
    void deleteUser(Long id) throws UserNotFoundException;
}
