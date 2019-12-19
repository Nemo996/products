package com.example.test.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserProfile(@PrimaryKey(autoGenerate = true)
                       val id:Int,
                       var username: String? = null,
                       var name:String? = null,
                       var imageUri: String? = null )