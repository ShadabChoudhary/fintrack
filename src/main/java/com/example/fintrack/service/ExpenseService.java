package com.example.fintrack.service;

import com.example.fintrack.dto.ExpenseDetailResDto;
import com.example.fintrack.dto.ExpenseReqDto;
import com.example.fintrack.dto.ExpenseResDto;
import com.example.fintrack.dto.GetAllExpenseResDto;
import com.example.fintrack.exception.UserNotFoundException;
import com.example.fintrack.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
    ExpenseResDto createExpense(String description, double amount, LocalDate date, boolean isRecurring,
                                String category, User user, MultipartFile receipt) throws UserNotFoundException, IOException;

    List<GetAllExpenseResDto> getAllExpense(int pageNumber, int pageSize, String sortBy, User user);
    ExpenseDetailResDto updateExpense(Long expenseId, ExpenseReqDto expenseReqDto, User user);

    ExpenseDetailResDto getExpenseById(Long expenseId, String name) throws AccessDeniedException;
}
