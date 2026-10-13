package com.example.expense_tracker
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.util.UUID

@Repository
interface ExpenseRepository : JpaRepository<Expense, UUID> {
    fun findByCategory(category: Category): List<Expense>

    @Query(
        """
    SELECT e FROM Expense e
    WHERE (TRUE = :#{#category == null} OR e.category = :category)
      AND (TRUE = :#{#minAmount == null} OR e.amount >= :minAmount)
      AND (TRUE = :#{#fromDate == null} OR e.date >= :fromDate)
      AND (TRUE = :#{#toDate == null} OR e.date <= :toDate)
"""
    )
    fun findExpenses(
        @Param("category") category: Category?,
        @Param("minAmount") minAmount: Double?,
        @Param("fromDate") fromDate: LocalDate?,
        @Param("toDate") toDate: LocalDate?
    ): List<Expense>
}

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