package com.example.test.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserProfile(@PrimaryKey(autoGenerate = true)
                       val id:Int,
                       val username: String?,
                       val imageUri: String? ) {
}