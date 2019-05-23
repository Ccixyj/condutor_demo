package me.xyj.conduct.demo.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import leakcanary.LeakSentry

class HomeViewModel() : ViewModel() {


    init {
        LeakSentry.refWatcher.watch(this)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("HomeViewModel" ,"onCleared")
    }

}