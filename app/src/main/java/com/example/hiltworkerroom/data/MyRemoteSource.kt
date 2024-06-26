package com.example.hiltworkerroom.data

import com.example.hiltworkerroom.model.remote.PostAdd
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class MyRemoteSource @Inject constructor(
    private val client: HttpClient
) {
    suspend fun post(
        title: String,
        description: String
    ) = client.post("https://dummyjson.com/posts/add"){
        setBody(PostAdd(title, description))
        contentType(ContentType.Application.Json)
    }
}