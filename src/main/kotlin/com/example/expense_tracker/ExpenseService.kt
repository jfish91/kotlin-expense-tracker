package com.example.expense_tracker
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.Month

// repository abilities:
//save()
//findAll()
//findById()
//existsById()
//deleteById()
//count()
//deleteAll()

@Service
class ExpenseService(
    private val repository: ExpenseRepository
) {
    fun getExpenses(): List<Expense> {
        return repository.findAll()
    }

    fun addExpense(request: CreateExpenseRequest): Expense {
        val expense = Expense(
            vendor = request.vendor,
            description = request.description ?: "",
            amount = request.amount,
            category = request.category,
            date = request.date,
            createdAt = LocalDateTime.now(),
        )

        repository.save(expense)
        return expense
    }

//    fun getTotal(): Double {
//        return repository.getAll()
//            .sumOf { it.amount }
//    }
//
//    fun getExpensesByCategory(category: Category): List<Expense> {
//        return repository.getAll()
//            .filter { it.category == category }
//    }
//
//    fun getExpensesByMonth(month: Month): List<Expense> {
//        return repository.getAll() // TODO: add filter later once dates are added
//    }
}