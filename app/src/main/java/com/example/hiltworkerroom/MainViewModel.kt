package com.example.hiltworkerroom

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import com.example.hiltworkerroom.data.MyRoomDb
import com.example.hiltworkerroom.model.entity.PostAddTemporaryEntity
import com.example.hiltworkerroom.worker.PostAddWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val db: MyRoomDb
) : ViewModel() {
    private val dao = db.dao

    val worker = OneTimeWorkRequestBuilder<PostAddWorker>()
        .apply {
            setConstraints(
                Constraints(
                    requiredNetworkType = NetworkType.CONNECTED
                )
            )
        }.build()
    val title = mutableStateOf("")
    val description = mutableStateOf("")

    fun insert(){
        viewModelScope.launch {
            dao.insert(
                PostAddTemporaryEntity(
                    title = title.value,
                    description = description.value
                )
            )
        }
    }
}