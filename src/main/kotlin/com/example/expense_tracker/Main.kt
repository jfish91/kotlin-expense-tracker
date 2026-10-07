package com.example.expense_tracker
//
//import java.time.LocalDate
//
//fun main() {
//
//    val repository = ExpenseRepository()
//    val tracker = ExpenseService(repository)
//
//    val dinner = Expense(
//        vendor = "Wendy's",
//        description = "Dinner",
//        amount = 24.50,
//        category = Category.DINING,
////        date = LocalDate.of(2026, 10, 7)
//    )
//
//    val groceries = Expense(
//        vendor =  "Kroger",
//        description = "Groceries",
//        amount = 64.50,
//        category = Category.GROCERIES,
////        date = LocalDate.of(2026, 10, 7)
//    )
//
//
//    tracker.addExpense(dinner)
//    tracker.addExpense(groceries)
//
//    println("Total spending: $${tracker.getTotal()}")
//
//    val allGroceries = tracker.getExpensesByCategory(Category.GROCERIES)
//    println(allGroceries)
//
//}