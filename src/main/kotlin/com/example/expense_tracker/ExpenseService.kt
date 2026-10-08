package com.example.expense_tracker
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.Month
import java.util.Optional
import java.util.UUID
import com.example.expense_tracker.exception.ExpenseNotFoundException

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
    fun getExpense(id: UUID): Expense {
        return repository.findById(id).orElseThrow { ExpenseNotFoundException(id)}
    }

    fun deleteExpense(id: UUID): String {
        val expense = getExpense(id)
        repository.deleteById(id)
        return "Deleted  $id  successfully"
         //.orElseThrow (ExpenseNotFoundException(id))
    }

    fun updateExpense(id: UUID, request: UpdateExpenseRequest): Expense {
        val currentExpense = getExpense(id)
        val newExpense = Expense(
            id = currentExpense.id,
            vendor = request.vendor,
            description = request.description ?: "",
            amount = request.amount,
            category = request.category,
            date = request.date,
            createdAt = currentExpense.createdAt,
        )
        println(newExpense)
        repository.save(newExpense)
        return newExpense
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