package me.xyj.conduct.demo.vm.base

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.archlifecycle.ControllerLifecycleOwner
import com.bluelinelabs.conductor.archlifecycle.LifecycleController

abstract class ViewModelController : LifecycleController {

    private val viewModelStore = ViewModelStore()

    constructor() : super()

    constructor(bundle: Bundle) : super(bundle)

    @JvmOverloads
    fun viewModelProvider(
        factory: ViewModelProvider.NewInstanceFactory = ViewModelProvider.AndroidViewModelFactory(
            activity!!.application
        )
    ): ViewModelProvider {
        return ViewModelProvider(viewModelStore, factory)
    }

    fun viewModelProvider(factory: ViewModelProvider.Factory): ViewModelProvider {
        return ViewModelProvider(viewModelStore, factory)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
    }



    companion object {
        fun ViewModelController.of(factory: ViewModelProvider.Factory? = null): ViewModelProvider {
            return if (factory == null) this.viewModelProvider()
            else this.viewModelProvider(factory)
        }
    }
}
