package com.example.fintrack.service;

import com.example.fintrack.dto.ExpenseDetailResDto;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.AccessDeniedException;
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

    @Autowired
    S3ServiceImpl s3Service;

    @Override
    public ExpenseResDto createExpense(String description, double amount, LocalDate date,
                                       boolean isRecurring, String category, String email,
                                       MultipartFile receipt) throws UserNotFoundException, IOException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not Found with this email" + email));

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

        //get and upload the bill
        if(receipt != null && !receipt.isEmpty()){
            String key = s3Service.uploadFile(receipt, email);
            expense.setReceiptKey(key);
        }
        expenseRepository.save(expense);

        // response as dto
        ExpenseResDto resDto = new ExpenseResDto();
        resDto.setExpenseId(expense.getId());
        resDto.setMessage("Expense created successfully");

        return resDto;
    }

    public List<GetAllExpenseResDto> getAllExpense(int pageNumber, int pageSize, String sortBy, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with this email" + email));

        Pageable pageable = PageRequest.of(
                Math.max(pageNumber, 0), //default page pageNumber
                pageSize <= 0 ? 10 : pageSize, //default pageSize
                Sort.by(sortBy).ascending()
        );

        Page<Expense> expensePage = expenseRepository.findByUser(user, pageable);

        return expensePage.stream()
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
    public ExpenseDetailResDto updateExpense(Long expenseId, ExpenseReqDto expenseReqDto, String email) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException("No Expense found with this ID"));

        User user = userRepository.findByEmail(email)
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
        ExpenseDetailResDto resDto = new ExpenseDetailResDto();
        resDto.setExpenseId(updateExpense.getId());
        resDto.setDescription(updateExpense.getDescription());
        resDto.setAmount(updateExpense.getAmount());
        resDto.setDate(updateExpense.getExpenseDate());
        resDto.setRecurring(updateExpense.isRecurring());
        resDto.setCategoryName(updateExpense.getCategory().getName());

        return resDto;
    }

    @Override
    public ExpenseDetailResDto getExpenseById(Long expenseId, String email) throws AccessDeniedException {
        Expense getExpense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));

        // Ensuring the expense belongs to the authenticated user
        if(!getExpense.getUser().getEmail().equals(email)){
            throw new AccessDeniedException("Unauthorized to access this expense");
        }

        //dto mapping
        ExpenseDetailResDto resDto = new ExpenseDetailResDto();
        resDto.setExpenseId(getExpense.getId());
        resDto.setDescription(getExpense.getDescription());
        resDto.setAmount(getExpense.getAmount());
        resDto.setCategoryName(getExpense.getCategory().getName());
        resDto.setRecurring(getExpense.isRecurring());

        return resDto;
    }


}
