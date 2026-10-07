package com.example.expense_tracker

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@RestController
@RequestMapping("/expenses")
class ExpenseController(private val expenseService: ExpenseService) {

    @GetMapping
    fun getExpenses(): List<Expense> {
        return expenseService.getExpenses()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addExpense(@Valid @RequestBody request: CreateExpenseRequest): Expense {
        return expenseService.addExpense(request)
    }
}