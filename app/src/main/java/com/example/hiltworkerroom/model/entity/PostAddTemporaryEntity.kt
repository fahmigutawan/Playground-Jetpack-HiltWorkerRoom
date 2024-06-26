package com.example.hiltworkerroom.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostAddTemporaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String
)
