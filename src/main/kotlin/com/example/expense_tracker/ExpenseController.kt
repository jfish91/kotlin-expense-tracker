package com.example.expense_tracker

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.Optional
import java.util.UUID

@RestController
@RequestMapping("/expenses")
class ExpenseController(private val expenseService: ExpenseService) {

    @GetMapping
    fun getExpenses(): List<Expense> {
        return expenseService.getExpenses()
    }

    @GetMapping("/{id}")
    fun getExpense(@PathVariable id: UUID): Expense {
        return expenseService.getExpense(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addExpense(@Valid @RequestBody request: CreateExpenseRequest): Expense {
        return expenseService.addExpense(request)
    }

    @DeleteMapping("/{id}")
    fun deleteExpense(@PathVariable id: UUID): String {
        return expenseService.deleteExpense(id)
    }

    @PutMapping("/{id}")
    fun updateExpense(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateExpenseRequest
    ): Expense {
        println("IN PUT MAPPING")
        return expenseService.updateExpense(id, request)
    }
}