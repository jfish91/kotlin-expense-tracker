package com.example.expense_tracker

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity                     // indicates it should be stored in the database
@Table(name = "expenses")   // indicates which table it should be stored in
data class Expense(
    @Id                     // indicates it is the primary key for this table
    @GeneratedValue         // generates the UUID automatically
    val id: UUID? = null,

    val vendor: String,

    val description: String?,

    val amount: Double,

    @Enumerated(EnumType.STRING)
    val category: Category,

    val date: LocalDate,

    val createdAt: LocalDateTime
)