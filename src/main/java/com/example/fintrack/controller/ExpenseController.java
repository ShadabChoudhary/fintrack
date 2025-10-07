package com.example.fintrack.controller;

import com.example.fintrack.dto.ExpenseDetailResDto;
import com.example.fintrack.dto.ExpenseReqDto;
import com.example.fintrack.dto.ExpenseResDto;
import com.example.fintrack.dto.GetAllExpenseResDto;
import com.example.fintrack.exception.UserNotFoundException;
import com.example.fintrack.model.User;
import com.example.fintrack.service.ExpenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final ObjectMapper objectMapper;

    public ExpenseController(ExpenseService expenseService, ObjectMapper objectMapper){
        this.expenseService = expenseService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ExpenseResDto> createExpResDto(@RequestPart("expense") String expenseDataJson,
                                                         @RequestPart(value = "file", required = false) MultipartFile receipt,
                                                         @AuthenticationPrincipal User user) throws UserNotFoundException, IOException {

        //Manually deserializing the JSON to DTO Object
        ExpenseReqDto createExpReqDto = objectMapper.readValue(expenseDataJson, ExpenseReqDto.class);

        ExpenseResDto responseDto = expenseService.createExpense(
                createExpReqDto.getDescription(),
                createExpReqDto.getAmount(),
                createExpReqDto.getDate(),
                createExpReqDto.isRecurring(),
                createExpReqDto.getCategory(),
                user,
                receipt
        );

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/user/all-expenses")
    public ResponseEntity<List<GetAllExpenseResDto>> getAllExpense(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "amount") String sortBy,
            @AuthenticationPrincipal User user){

        List<GetAllExpenseResDto> getAllExpenseResDto = expenseService.getAllExpense(pageNumber, pageSize,
                                                        sortBy, user);

        return new ResponseEntity<>(getAllExpenseResDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDetailResDto> updateExpense(@PathVariable("id") Long expenseId,
                                       @RequestBody ExpenseReqDto expenseReqDto, @AuthenticationPrincipal User user){

        ExpenseDetailResDto responseDto = expenseService.updateExpense(expenseId, expenseReqDto, user);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDetailResDto> getSingleExpense(@PathVariable("id") Long expenseId,
                                                                @AuthenticationPrincipal User user) throws AccessDeniedException {
        ExpenseDetailResDto expenseDetailResDto = expenseService.getExpenseById(expenseId, user.getEmail());

        return new ResponseEntity<>(expenseDetailResDto, HttpStatus.OK);
    }
}
