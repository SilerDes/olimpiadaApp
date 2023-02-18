package com.kazbekov.olimpiada

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kazbekov.olimpiada.data.ServiceVK
import com.kazbekov.olimpiada.network.Repository
import com.kazbekov.olimpiada.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository(application)

    private val servicesLiveData = MutableLiveData<List<ServiceVK>>(emptyList())
    val services: LiveData<List<ServiceVK>>
        get() = servicesLiveData

    private val downloadFailedLiveData = SingleLiveEvent<Unit>()
    val downloadFailed: LiveData<Unit>
        get() = downloadFailedLiveData

    private suspend fun downloadFile() = withContext(Dispatchers.IO) {
        repository.downloadFile().takeIf { it }?.let {
            servicesLiveData.postValue(repository.parseServices())
        } ?: downloadFailedLiveData.postValue(Unit)
    }

    fun getVKServices() {
        viewModelScope.launch {
            if (repository.needDownload()) {
                downloadFile()
            } else {
                servicesLiveData.postValue(repository.parseServices())
            }
        }
    }
}