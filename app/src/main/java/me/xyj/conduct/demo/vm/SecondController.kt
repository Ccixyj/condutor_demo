package me.xyj.conduct.demo.vm

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import leakcanary.LeakSentry
import me.xyj.conduct.demo.vm.base.ViewModelController
import org.koin.core.KoinComponent
import org.koin.core.get

class SecondController : ViewModelController(), KoinComponent {


    private val vm = this.of(get<SecondViewModel.Factory>()).get(SecondViewModel::class.java)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        Log.d("SecondController", "onCreateView $vm")
        lifecycle.addObserver(vm)

        return TextView(inflater.context).apply {
            text = this@SecondController::class.java.name
            vm.live.observe(this@SecondController, Observer {
                this.append("\r\n" + it)
            })

            setOnClickListener {

            }
        }
    }

    override fun onDestroy() {
        Log.d("SecondController", "onDestroy   $vm")
        lifecycle.removeObserver(vm)
        super.onDestroy()
        LeakSentry.refWatcher.watch(this)
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        Log.d("SecondController", "onDestroyView $view")
    }

}