package com.example.expense_tracker

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.time.LocalDate

//@NotBlank
//@Positive
//@Size
//@Min
//@Max

data class CreateExpenseRequest(

    @field:NotBlank(message = "vendor cannot be blank")
    val vendor: String,

    val description: String?,

    @field:Positive(message = "expense amount must be positive")
    val amount: Double,

    val category: Category,

    val date : LocalDate,
)