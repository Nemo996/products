package com.example.test.data.product_list

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    val id: Int,
    val img: String,
    val text: String,
    val title: String
)