package com.example.expense_tracker.exception

import java.util.UUID

// keep exception itself dumb, dont add status codes or specific logic inside
class ExpenseNotFoundException(id: UUID) :
    RuntimeException("Expense with id $id was not found")