package com.example.expense_tracker

import com.example.expense_tracker.exception.ExpenseNotFoundException
import io.mockk.Awaits

import java.time.LocalDateTime
import java.time.LocalDate
import java.util.UUID
import java.util.Optional

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.slot
import io.mockk.just
import io.mockk.Runs

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

import org.junit.jupiter.api.assertThrows


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

        // give mockk info to not query PostgreSQL, do what this says instead
        // stubbing: define return values using every {} block
        every {
            // when someone calls repository.save() and passes an Expense object, put it into expenseSlot
            repository.save(capture(expenseSlot))
        } answers {
            // telling what the mocked repository should return
            // firstArg is first thing passes into the function, in this case the Expense object
            // simulates repository saving an Expense and returning it
            firstArg<Expense>().copy(id = generatedId)
        }
        // use every {} answers {} when the mock's behavior needs to be dynamic or depend on arg's passed into it

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

    @Test
    fun `getExpense returns expense from repository`() {
    // Arrange - create data to test on
        val id = UUID.randomUUID()
        val expense = Expense(
            id = id,
            vendor = "Kroger",
            description = "Groceries",
            amount = 45.50,
            category = Category.GROCERIES,
            date = LocalDate.of(2026, 11, 5),
            createdAt = LocalDateTime.now()
        )

        every {
            repository.findById(id)
        } returns Optional.of(expense) // this simulates a database lookup that should succeed
        // use every {} returns when the mock should return a specific value

    // Act - call actual service method
        val result = service.getExpense(id)

    // Assert - verify results match
        assertEquals(expense, result)

        verify(exactly = 1) {repository.findById(id)}

    }

    @Test
    fun `getExpense throws ExpenseNotFoundException if not found`() {
    // Arrange
        val id = UUID.randomUUID()
        // when expense doesn't exist, nothing should be returned
        every { repository.findById(id) } returns Optional.empty()

    // Act + Assert together
        assertThrows<ExpenseNotFoundException> { service.getExpense(id) }
        // executes service,getExpense(id)
        // if it throws the ExpenseNotFoundException, the test passes, otherwise fails

        verify(exactly = 1) { repository.findById(id) }

    }

    @Test
    fun `deleteExpense deletes expense from repository`() {
        // want to verify the service looks up the expense and deletes if it exists
    // Arrange
        val id = UUID.randomUUID()

        val expense = Expense(
            id = id,
            vendor = "Kroger",
            description = "Groceries",
            amount = 45.50,
            category = Category.GROCERIES,
            date = LocalDate.of(2026, 11, 5),
            createdAt = LocalDateTime.now()
        )

        // setup mock behavior when service tries to look it up
        // if repository.findById(id) is ever run, pretend the repo found the expense and returned it
        every {repository.findById(id)} returns Optional.of(expense)
        // when this method runs, do nothing and return normally (only need to mock the expense lookup)
        every { repository.deleteById(id) } just Runs

    // Act
        val result = service.deleteExpense(id)

    // Assert
        // want to match on returned string
        assertEquals("Deleted $id successfully", result)

        verify(exactly = 1) { repository.deleteById(id) }
    }

    @Test
    fun `deleteExpense throws ExpenseNotFoundException if expense not found`() {
    // Arrange
        val id = UUID.randomUUID()

        every { repository.findById(id) } returns Optional.empty()
    // Act + Assert together
        assertThrows<ExpenseNotFoundException> { service.deleteExpense(id) }
        // executes service,deleteExpense(id)
        // if it throws the ExpenseNotFoundException, the test passes, otherwise fails

        // want to verify that lookup occurred, but delete never did since exception was thrown
        verify(exactly = 1) { repository.findById(id) }
        verify(exactly = 0) { repository.deleteById(id) }
    }

    @Test
    fun `updateExpense modifies existing expense`() {
    // Arrange
        val expenseSlot = slot<Expense>()

        val id = UUID.randomUUID()
        val expense = Expense(
            id = id,
            vendor = "Kroger",
            description = "Groceries",
            amount = 45.50,
            category = Category.GROCERIES,
            date = LocalDate.of(2026, 11, 5),
            createdAt = LocalDateTime.now()
        )

        val request = UpdateExpenseRequest(
            vendor = "United",
            description = "Flight",
            amount = 155.50,
            category = Category.TRAVEL,
            date = LocalDate.of(2026, 11, 5),
        )

        every {repository.findById(id)} returns Optional.of(expense)
        // whenever repository.save() runs, return whatever object was sent into it
        every {repository.save(capture(expenseSlot))
        } answers {firstArg<Expense>()}

    // Act
        val result = service.updateExpense(id, request)

    // Assert
        // could also use expenseSlot.captured for these fields
        assertEquals("United", result.vendor)
        assertEquals("Flight", result.description)
        assertEquals(155.50, result.amount)
        assertEquals(Category.TRAVEL, result.category)
        assertEquals(request.date, result.date)

        assertEquals(expense.id, expenseSlot.captured.id)
        assertEquals(expense.createdAt, expenseSlot.captured.createdAt)

        verify(exactly = 1) {repository.findById(id)}
        verify(exactly = 1) { repository.save(any()) }
    }

    @Test
    fun `updateExpense throws ExpenseNotFoundException if expense not found`() {
    // Arrange
        val id = UUID.randomUUID()
        val request = UpdateExpenseRequest(
            vendor = "United",
            description = "Flight",
            amount = 155.50,
            category = Category.TRAVEL,
            date = LocalDate.of(2026, 11, 5),
        )
        every { repository.findById(id) } returns Optional.empty()

    // Act and Assert
        assertThrows<ExpenseNotFoundException> { service.updateExpense(id, request) }

        verify(exactly = 1) { repository.findById(id) }
        verify(exactly = 0) { repository.save(any()) }
    }

    @Test
    fun `updateExpense saves empty description`() {
        // Arrange
        val expenseSlot = slot<Expense>()

        val id = UUID.randomUUID()
        val expense = Expense(
            id = id,
            vendor = "Kroger",
            description = "Groceries",
            amount = 45.50,
            category = Category.GROCERIES,
            date = LocalDate.of(2026, 11, 5),
            createdAt = LocalDateTime.now()
        )

        val request = UpdateExpenseRequest(
            vendor = "United",
            description = null,
            amount = 155.50,
            category = Category.TRAVEL,
            date = LocalDate.of(2026, 11, 5),
        )

        every { repository.findById(id) } returns Optional.of(expense)
        // whenever repository.save() runs, return whatever object was sent into it
        every {
            repository.save(capture(expenseSlot))
        } answers { firstArg<Expense>() }

        // Act
        val result = service.updateExpense(id, request)

        // Assert
        assertEquals("", result.description)

        verify(exactly = 1) { repository.findById(id) }
        verify(exactly = 1) { repository.save(any()) }
    }
}

