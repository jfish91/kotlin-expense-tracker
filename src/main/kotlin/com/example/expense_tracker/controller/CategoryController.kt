package com.example.expense_tracker.controller

import com.example.expense_tracker.Category

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/categories")
class CategoryController {

    @GetMapping
    fun getCategories(): List<String> {
        return Category.entries.map { it.name }
    }
}