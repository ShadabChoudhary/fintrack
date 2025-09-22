package com.example.fintrack.controller;

import com.example.fintrack.dto.SignInRequestDto;
import com.example.fintrack.dto.SignInResponseDto;
import com.example.fintrack.dto.SignUpRequestDto;
import com.example.fintrack.dto.SignUpResponseDto;
import com.example.fintrack.exception.InvalidCredentialsException;
import com.example.fintrack.exception.UserAlreadyExistException;
import com.example.fintrack.exception.UserNotFoundException;
import com.example.fintrack.model.User;
import com.example.fintrack.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> createUser(@RequestBody SignUpRequestDto requestDto) throws UserAlreadyExistException {
        SignUpResponseDto responseDto = userService.signUp(requestDto.getName(),
                                                            requestDto.getEmail(),
                                                            requestDto.getPassword());
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponseDto> logIn(@RequestBody SignInRequestDto requestDto) throws UserNotFoundException, InvalidCredentialsException {
        SignInResponseDto responseDto = userService.singIn(requestDto.getEmail(),
                                                            requestDto.getPassword());

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> delete() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        userService.deleteUser(user.getId());
        return ResponseEntity.ok("User deleted Successfully");
    }

}
