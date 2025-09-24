package com.example.fintrack.service;

import com.example.fintrack.dto.ExpenseReqDto;
import com.example.fintrack.dto.ExpenseResDto;
import com.example.fintrack.dto.GetAllExpenseResDto;
import com.example.fintrack.exception.UserNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
    ExpenseResDto createExpense(String description, double amount, LocalDate date, boolean isRecurring,
                                String category, String email) throws UserNotFoundException;

    List<GetAllExpenseResDto> getAllExpense(int pageNumber, int pageSize, String sortBy, String email);
    ExpenseResDto updateExpense(Long expenseId, ExpenseReqDto expenseReqDto, String email);
}
