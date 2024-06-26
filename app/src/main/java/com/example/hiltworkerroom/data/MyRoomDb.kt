package com.example.hiltworkerroom.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hiltworkerroom.model.entity.PostAddTemporaryEntity

@Database(
    entities = [
        PostAddTemporaryEntity::class
    ],
    version = 1
)
abstract class MyRoomDb : RoomDatabase() {
    abstract val dao: MyLocalDao
}