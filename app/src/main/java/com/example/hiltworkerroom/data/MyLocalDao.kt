package com.example.hiltworkerroom.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.hiltworkerroom.model.entity.PostAddTemporaryEntity

@Dao
interface MyLocalDao {
    @Insert
    suspend fun insert(item: PostAddTemporaryEntity)

    @Query("select * from postaddtemporaryentity")
    suspend fun getAll(): List<PostAddTemporaryEntity>

    @Query("delete from postaddtemporaryentity where id=:id")
    suspend fun deleteById(id: Int)
}