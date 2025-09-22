package com.example.fintrack.controller;

import com.example.fintrack.dto.ExpenseReqDto;
import com.example.fintrack.dto.ExpenseResDto;
import com.example.fintrack.dto.GetAllExpenseResDto;
import com.example.fintrack.exception.UserNotFoundException;
import com.example.fintrack.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService){
        this.expenseService = expenseService;
    }

    @PostMapping("/add")
    public ResponseEntity<ExpenseResDto> createExpResDto(@RequestBody ExpenseReqDto createExpReqDto,
                                                         Principal principal) throws UserNotFoundException {
        String email = principal.getName();
        ExpenseResDto responseDto = expenseService.createExpense(
                createExpReqDto.getDescription(),
                createExpReqDto.getAmount(),
                createExpReqDto.getDate(),
                createExpReqDto.isRecurring(),
                createExpReqDto.getCategory(),
                email
        );

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/user/all-expenses")
    public ResponseEntity<List<GetAllExpenseResDto>> getAllExpense(Principal principal){
        String email = principal.getName();
        List<GetAllExpenseResDto> getAllExpenseResDto = expenseService.getAllExpense(email);

        return new ResponseEntity<>(getAllExpenseResDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResDto> updateExpense(@PathVariable("id") Long expenseId,
                                       @RequestBody ExpenseReqDto expenseReqDto, Principal principal){
        String email = principal.getName();
        ExpenseResDto responseDto = expenseService.updateExpense(expenseId, expenseReqDto, email);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
