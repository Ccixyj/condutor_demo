package me.xyj.conduct.demo.vm

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import leakcanary.LeakSentry
import org.koin.core.KoinComponent
import org.koin.core.get

class HomeController : LifecycleController(), KoinComponent {

    init {
        LeakSentry.refWatcher.watch(this)
    }

    private val vm by lazy {
        ViewModelProviders.of(activity as FragmentActivity).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        Log.d("HomeController", "onCreateView $vm")


        return TextView(inflater.context).apply {
            text = this@HomeController::class.java.name
            setOnClickListener {
                router.pushController(
                    RouterTransaction.with(SecondController()).pushChangeHandler(
                        get<HorizontalChangeHandler>()
                    )
                        .popChangeHandler(get<FadeChangeHandler>())
                )
            }
        }
    }

    override fun onDestroy() {

        Log.d("HomeController", "onDestroy   $vm")
        super.onDestroy()
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        Log.d("HomeController", "onDestroyView $view")
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        Log.d("HomeController", "onDetach   $view")
    }
}