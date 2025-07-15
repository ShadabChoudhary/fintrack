package com.example.fintrack.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GetAllExpenseResDto {
    private Long expenseId;
    private String description;
    private Double amount;
    private LocalDate date;
    private String categoryName;
    private boolean isRecurring;
}
