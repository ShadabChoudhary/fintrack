package com.example.fintrack.service;

import com.example.fintrack.dto.SignInResponseDto;
import com.example.fintrack.dto.SignUpResponseDto;
import com.example.fintrack.exception.InvalidCredentialsException;
import com.example.fintrack.exception.UserAlreadyExistException;
import com.example.fintrack.exception.UserNotFoundException;
import com.example.fintrack.model.User;
import com.example.fintrack.repository.UserRepository;
import com.example.fintrack.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public SignUpResponseDto signUp(String name, String email, String password) throws UserAlreadyExistException {
        Optional<User> getUser = userRepository.findByEmail(email);

        if(getUser.isPresent()){
            throw new UserAlreadyExistException("User Already Exist with this email: "+email);
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        // return a response DTO to avoid sensitive data leak
        SignUpResponseDto response = new SignUpResponseDto();
        response.setUserId(user.getId());
        response.setMessage("User registered successfully");

        return response;
    }

    @Override
    public SignInResponseDto singIn(String email, String password) throws UserNotFoundException, InvalidCredentialsException {
        User getUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("No User found with this email"));

        if(getUser.isDeleted()){
            throw new IllegalStateException("Your account has been deactivated.");
        }

        if(!passwordEncoder.matches(password, getUser.getPassword())){
            throw new InvalidCredentialsException("Email or Password is incorrect");
        }

        //Generate token
        String token = jwtUtil.generateToken(getUser);

        // return a response DTO
        SignInResponseDto response = new SignInResponseDto();
        response.setUserId(getUser.getId());
        response.setMessage("SignIn successful");
        response.setToken(token);

        return response;
    }

    @Override
    public void deleteUser(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        if(user.isDeleted()){
            throw new IllegalStateException("User already deleted");
        }
        user.setDeleted(true);
        userRepository.save(user);
    }
}
