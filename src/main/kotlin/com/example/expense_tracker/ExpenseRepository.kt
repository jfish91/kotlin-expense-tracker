package com.example.expense_tracker
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

@Repository
interface ExpenseRepository : JpaRepository<Expense, UUID>

// pre-database setup in-memory repository
//@Repository
//class ExpenseRepository {
//
//    private val expenses = mutableListOf<Expense>()
//
//    fun add(expense: Expense) {
//        expenses.add(expense)
//    }
//
//    fun getAll(): List<Expense> {
//        return expenses
//    }
//}