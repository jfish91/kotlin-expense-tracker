package com.example.expense_tracker

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

import kotlin.test.Test
import kotlin.test.assertEquals

class ExpenseServiceTest {

    // create mock repository and put into ExpenseService
    // manually providing simulated repository
    private val repository = mockk<ExpenseRepository>()
    private val service = ExpenseService(repository)

    @Test
    fun `getExpenses returns expenses from repository`(){
    // Arrange
        val expectedExpenses = emptyList<Expense>()
        // tells mockk what to do when service calls the repository
        every {
            repository.findExpenses(null,null,null,null)
        } returns expectedExpenses
    // Act - call actual service method we want to test
        val result = service.getExpenses(null,null,null, null)
    // Assert - verify if service returned expected result
        assertEquals(expectedExpenses, result)
        // verifies method was called exactly once
        verify(exactly = 1) {
            repository.findExpenses(null,null,null,null)
        }
    }
}