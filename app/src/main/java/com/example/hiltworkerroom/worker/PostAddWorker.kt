package com.example.hiltworkerroom.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.room.Room
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.hiltworkerroom.data.MyLocalDao
import com.example.hiltworkerroom.data.MyRemoteSource
import com.example.hiltworkerroom.data.MyRoomDb
import com.example.hiltworkerroom.model.remote.PostAdd
import com.example.hiltworkerroom.util.NotificationHandler
import com.example.hiltworkerroom.util.NotificationId
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

@HiltWorker
class PostAddWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    val db = Room.databaseBuilder(
        context = context,
        klass = MyRoomDb::class.java,
        name = "vobis-akuntansi-db-2"
    ).build()
    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            gson()
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 20000
            connectTimeoutMillis = 20000
            socketTimeoutMillis = 20000
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("HTTP Call", message)
                }
            }
            level = LogLevel.ALL
        }
    }
    val dao = db.dao
    val remote = MyRemoteSource(client)

    override fun doWork(): Result {
        return try {
            runBlocking {
                val entities = dao.getAll()
                val responses = entities.map { async { remote.post(it.title, it.description) } }
                responses.forEach { it.await().body<PostAdd>() }
                entities.forEach { dao.deleteById(it.id) }
            }

            NotificationHandler.dismissNotification(context, NotificationId.POST_ADD)
            NotificationHandler.showNotification(
                context = context,
                channelId = "123",
                channelName = "TEST",
                title = "ADD POST",
                content = "Semua data berhasil ditambahkan",
                notificationId = NotificationId.POST_ADD
            )
            Result.success()
        } catch (e: Exception) {
            NotificationHandler.dismissNotification(context, NotificationId.POST_ADD)
            NotificationHandler.showNotification(
                context = context,
                channelId = "123",
                channelName = "TEST",
                title = "ADD POST",
                content = "Gagal dengan error: ${e.message}",
                notificationId = NotificationId.POST_ADD
            )
            Result.failure()
        }
    }
}