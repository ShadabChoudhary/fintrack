package com.example.fintrack.controller;

import com.example.fintrack.dto.SignInRequestDto;
import com.example.fintrack.dto.SignInResponseDto;
import com.example.fintrack.dto.SignUpRequestDto;
import com.example.fintrack.dto.SignUpResponseDto;
import com.example.fintrack.exception.InvalidCredentialsException;
import com.example.fintrack.exception.UserAlreadyExistException;
import com.example.fintrack.exception.UserNotFoundException;
import com.example.fintrack.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws UserNotFoundException {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted Successfully");
    }

}
