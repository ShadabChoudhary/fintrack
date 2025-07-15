package com.example.fintrack.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class User extends BaseModel {
    private String name;
    private String email;
    private String password;
    @OneToMany
    private List<Expense> expenseList;

}
