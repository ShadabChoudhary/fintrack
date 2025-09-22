package com.example.fintrack.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExpenseReqDto {
    private String description;
    private double amount;
    private LocalDate date;
    private boolean isRecurring;
    private String category;
}
