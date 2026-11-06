package com.example.expense_tracker

import java.time.LocalDate
import java.util.UUID
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.slot

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

import io.mockk.just
import io.mockk.Runs

import kotlin.test.assertTrue

class ExpenseServiceTest {

    // create mock repository and put into ExpenseService
    // manually providing simulated repository
    private val repository = mockk<ExpenseRepository>()
    private val service = ExpenseService(repository)

    @Test
    fun `getExpenses returns expenses from repository`() {
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
    @Test
    fun `addExpense creates and saves an expense`() {
        // NOTE: slot and capture may not be needed here
        // otherwise they store what the service constructs BEFORE saving to db

    // Arrange - create data to test on
        val request = CreateExpenseRequest(
            vendor = "Kroger",
            description = "Groceries",
            amount = 45.50,
            category = Category.GROCERIES,
            date = LocalDate.of(2026, 11, 5),
        )
        // container that temporarily holds an object, in this case an expense
        val expenseSlot = slot<Expense>()
        val generatedId = UUID.randomUUID()

//        give mockk info to not query PostgreSQL, do what this says instead
        every {
            // when someone calls repository.save() and passes an Expense object, put it into expenseSlot
            repository.save(capture(expenseSlot))
        } answers {
            // telling what the mocked repository should return
            // firstArg is first thing passes into the function, in this case the Expense object
            // simulates repository saving an Expense and returning it
            firstArg<Expense>().copy(id = generatedId)
        }

    // Act - call actual service method we want to test
        val result = service.addExpense(request)
        // when this line runs, the expenseSlot now contains the Expense object that was passed to save

    // Assert - verify if service returned expected result
        assertEquals("Kroger", result.vendor)
        assertEquals("Groceries", result.description)
        assertEquals(45.50, result.amount)
        assertEquals(Category.GROCERIES, result.category)
        assertEquals(request.date, result.date)

        assertEquals(generatedId, result.id) // prev generated id should match result's id
        assertNotNull(result.createdAt) // result should have createdAt
        assertNull(expenseSlot.captured.id) // captured expense should not have an id

        verify(exactly = 1) {
            repository.save(any())
        }
    }

    @Test
    fun `addExpense creates and saves empty description`(){
    // Arrange
        val request = CreateExpenseRequest(
            vendor = "Kroger",
            description = null,
            amount = 45.50,
            category = Category.GROCERIES,
            date = LocalDate.of(2026, 11, 5),
        )
        val expenseSlot = slot<Expense>()
        val generatedId = UUID.randomUUID()

        every {
            repository.save(capture(expenseSlot))
        } answers {
            firstArg<Expense>().copy(id = generatedId)
        }

    // Act
        val result = service.addExpense(request)

    // Assert
        assertEquals("Kroger", result.vendor)
        assertEquals("", result.description) // null value should be converted to empty string when saved
        assertEquals(45.50, result.amount)
        assertEquals(Category.GROCERIES, result.category)
        assertEquals(request.date, result.date)

        assertEquals(generatedId, result.id)
        assertNotNull(result.createdAt)

        assertNull(expenseSlot.captured.id)

        verify(exactly = 1) {
            repository.save(any())
        }
    }


}