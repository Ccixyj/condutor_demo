package me.xyj.conduct.demo.vm

import android.util.Log
import androidx.lifecycle.*
import leakcanary.LeakSentry

class HomeViewModel : ViewModel(), LifecycleObserver {

    val live = MutableLiveData<String>()

    init {
        LeakSentry.refWatcher.watch(this)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("HomeViewModel", "onCleared")
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun event(owner: LifecycleOwner, event: Lifecycle.Event) {
        Log.d("SecondViewModel", "$event / ${owner.lifecycle.currentState}: owner -> $owner ")
        live.value = "$event / ${owner.lifecycle.currentState}: owner -> $owner "
    }
}