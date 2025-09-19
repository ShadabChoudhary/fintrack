package com.example.fintrack.controller;

import com.example.fintrack.dto.ExpenseReqDto;
import com.example.fintrack.dto.ExpenseResDto;
import com.example.fintrack.dto.GetAllExpenseResDto;
import com.example.fintrack.exception.UserNotFoundException;
import com.example.fintrack.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService){
        this.expenseService = expenseService;
    }

    @PostMapping("/add")
    public ResponseEntity<ExpenseResDto> createExpResDto(@RequestBody ExpenseReqDto createExpReqDto) throws UserNotFoundException {
        ExpenseResDto responseDto = expenseService.createExpense(
                createExpReqDto.getDescription(),
                createExpReqDto.getAmount(),
                createExpReqDto.getDate(),
                createExpReqDto.isRecurring(),
                createExpReqDto.getCategory(),
                createExpReqDto.getUserId()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<GetAllExpenseResDto>> getAllExpenseByUserId(@PathVariable Long id){
        List<GetAllExpenseResDto> getAllExpenseResDto = expenseService.getAllExpense(id);

        return new ResponseEntity<>(getAllExpenseResDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResDto> updateExpense(@PathVariable("id") Long expenseId, @RequestBody ExpenseReqDto expenseReqDto){
        ExpenseResDto responseDto = expenseService.updateExpense(expenseId, expenseReqDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
