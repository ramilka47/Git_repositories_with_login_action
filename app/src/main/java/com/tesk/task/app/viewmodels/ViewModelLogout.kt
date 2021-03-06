package com.tesk.task.app.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tesk.task.providers.git.GitService
import com.tesk.task.providers.room.AppDatabase
import kotlinx.coroutines.CoroutineScope

class ViewModelLogout(private val sharedPreferences: SharedPreferences) : ViewModel() {

    private val mutableLiveDataSuccess = MutableLiveData<Boolean>()
    private val mutableLiveDataLoading = MutableLiveData<Boolean>()
    private val mutableLiveDataError = MutableLiveData<Boolean>()

    val liveDataSuccess : LiveData<Boolean> = mutableLiveDataSuccess
    val liveDataLoading : LiveData<Boolean> = mutableLiveDataLoading
    val liveDataError : LiveData<Boolean> = mutableLiveDataError

    fun logout() =
            with(sharedPreferences.edit()) {

                this.remove(PreferenceUtil.TOKEN)
                this.apply()

                mutableLiveDataSuccess.postValue(true)
            }

}