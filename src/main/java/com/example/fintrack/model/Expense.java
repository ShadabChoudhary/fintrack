package com.example.fintrack.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Expense extends BaseModel{
    private String description;
    private double amount;
    private LocalDate expenseDate;
    private boolean isRecurring;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User user;

    private String receiptKey;
}
