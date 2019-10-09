package me.xyj.conduct.demo.vm

import android.util.Log
import androidx.lifecycle.*
import leakcanary.AppWatcher

class SecondViewModel(val demoRepo: DemoRepo) : ViewModel(), LifecycleObserver {


    val live = MutableLiveData<String>()

    override fun onCleared() {
        super.onCleared()
        Log.d("HomeViewModel", "onCleared")
        AppWatcher.objectWatcher.watch(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun event(owner: LifecycleOwner, event: Lifecycle.Event) {
        Log.d("SecondViewModel", "$event / ${owner.lifecycle.currentState}: owner -> $owner ")
        live.value = "$event / ${owner.lifecycle.currentState}: owner -> $owner "
    }

    class Factory(private val repo: DemoRepo) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SecondViewModel(repo) as T
        }
    }


}