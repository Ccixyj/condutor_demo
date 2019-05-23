package me.xyj.conduct.demo.vm

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import leakcanary.LeakSentry
import org.koin.core.KoinComponent
import org.koin.core.get

class SecondController : LifecycleController(), KoinComponent {

    init {
        LeakSentry.refWatcher.watch(this)
    }

    private val vm by lazy {
        ViewModelProviders.of(activity as FragmentActivity, get<SecondViewModel.Factory>()).get(SecondViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        Log.d("SecondController", "onCreateView $vm")


        return TextView(inflater.context).apply {
            text = this@SecondController::class.java.name
        }
    }

    override fun onDestroy() {

        Log.d("SecondController", "onDestroy   $vm")
        super.onDestroy()
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        Log.d("SecondController", "onDestroyView $view")
    }

}