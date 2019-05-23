package me.xyj.conduct.demo.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import leakcanary.LeakSentry

class SecondViewModel(val demoRepo: DemoRepo) : ViewModel() {


    init {
        LeakSentry.refWatcher.watch(this)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("SecondViewModel", "onCleared $demoRepo")
    }

    class Factory(private val repo: DemoRepo) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SecondViewModel(repo) as T
        }
    }


}