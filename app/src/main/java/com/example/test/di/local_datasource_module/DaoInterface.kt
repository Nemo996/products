package com.example.test.di.local_datasource_module

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DaoInterface {

  //  @Query("SELECT * FROM users")
   // fun findAll(): LiveData<List<GithubUser>>
}