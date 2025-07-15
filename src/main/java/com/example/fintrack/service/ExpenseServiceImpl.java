package com.example.fintrack.service;

import com.example.fintrack.dto.ExpenseReqDto;
import com.example.fintrack.dto.ExpenseResDto;
import com.example.fintrack.dto.GetAllExpenseResDto;
import com.example.fintrack.exception.ExpenseNotFoundException;
import com.example.fintrack.exception.UserNotFoundException;
import com.example.fintrack.model.Category;
import com.example.fintrack.model.Expense;
import com.example.fintrack.model.User;
import com.example.fintrack.repository.CategoryRepository;
import com.example.fintrack.repository.ExpenseRepository;
import com.example.fintrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ExpenseRepository expenseRepository;

    @Override
    public ExpenseResDto createExpense(String description, double amount, LocalDate date, boolean isRecurring, String category, Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not Found with this id"));

        Category cat = categoryRepository.findByNameIgnoreCase(category)
                .orElseGet(() -> {
                    Category newCat = new Category();
                    newCat.setName(category);
                    return categoryRepository.save(newCat);
                });

        // create Expense
        Expense expense = new Expense();
        expense.setDescription(description);
        expense.setAmount(amount);
        expense.setExpenseDate(date);
        expense.setRecurring(isRecurring);
        expense.setCategory(cat);
        expense.setUser(user);

        expenseRepository.save(expense);

        // response as dto
        ExpenseResDto resDto = new ExpenseResDto();
        resDto.setExpenseId(expense.getId());
        resDto.setMessage("Expense created successfully");

        return resDto;
    }

    public List<GetAllExpenseResDto> getAllExpense(Long userId) {
        List<Expense> expenses = expenseRepository.findAllByUserId(userId);

        return expenses.stream()
                        .map(expense -> {
                            GetAllExpenseResDto resDto = new GetAllExpenseResDto();
                            resDto.setExpenseId(expense.getId());
                            resDto.setDescription(expense.getDescription());
                            resDto.setAmount(expense.getAmount());
                            resDto.setDate(expense.getExpenseDate());
                            resDto.setRecurring(expense.isRecurring());
                            resDto.setCategoryName(expense.getCategory().getName());
                            return resDto;
                        })
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseResDto updateExpense(Long expenseId, ExpenseReqDto expenseReqDto) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException("No Expense found with this ID"));

        User user = userRepository.findById(expenseReqDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with this ID"));

        Category cat = categoryRepository.findByNameIgnoreCase(expenseReqDto.getCategory())
                .orElseGet(() -> {
                    Category newCat = new Category();
                    newCat.setName(expenseReqDto.getCategory());
                    return categoryRepository.save(newCat);
                });

        //update the fields
        expense.setDescription(expenseReqDto.getDescription());
        expense.setAmount(expenseReqDto.getAmount());
        expense.setExpenseDate(expenseReqDto.getDate());
        expense.setRecurring(expenseReqDto.isRecurring());
        expense.setCategory(cat);
        expense.setUser(user);

        Expense updateExpense = expenseRepository.save(expense);

        //Dto mapping
        ExpenseResDto resDto = new ExpenseResDto();
        resDto.setExpenseId(updateExpense.getId());
        resDto.setDescription(updateExpense.getDescription());
        resDto.setAmount(updateExpense.getAmount());
        resDto.setDate(updateExpense.getExpenseDate());
        resDto.setRecurring(updateExpense.isRecurring());
        resDto.setCategoryName(updateExpense.getCategory().getName());

        return resDto;
    }
}
