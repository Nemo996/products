package com.example.test.data

data class ReviewModel(
    val created_at: String,
    val created_by: CreatedBy,
    val id: Int,
    val product: Int,
    val rate: Int,
    val text: String
)