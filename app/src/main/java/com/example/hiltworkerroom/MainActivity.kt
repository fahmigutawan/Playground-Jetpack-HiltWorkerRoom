package com.example.hiltworkerroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.example.hiltworkerroom.ui.theme.HiltWorkerRoomTheme
import com.example.hiltworkerroom.util.NotificationHandler
import com.example.hiltworkerroom.util.NotificationId
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel by viewModels<MainViewModel>()
            val context = LocalContext.current
            val workManager = WorkManager.getInstance(context)

            HiltWorkerRoomTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = viewModel.title.value,
                                onValueChange = { viewModel.title.value = it },
                                label = {
                                    Text(text = "TITLE")
                                }
                            )

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = viewModel.description.value,
                                onValueChange = { viewModel.description.value = it },
                                label = {
                                    Text(text = "DESCRIPTION")
                                }
                            )

                            Button(
                                onClick = {
                                    NotificationHandler.showNotification(
                                        context = context,
                                        channelId = "123",
                                        channelName = "TEST",
                                        title = "ADD POST",
                                        content = "Tembak Add Post Sedang Berlangsung. Tunggu sebentar",
                                        notificationId = NotificationId.POST_ADD
                                    )

                                    viewModel.insert()
                                    workManager
                                        .beginUniqueWork(
                                            "POST ADD",
                                            ExistingWorkPolicy.REPLACE,
                                            viewModel.worker
                                        ).enqueue()
                                }
                            ) {
                                Text(text = "START WORKER")
                            }
                        }
                    }
                }
            }
        }
    }
}