package me.xyj.conduct.demo.vm

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import kotlinx.android.synthetic.main.controller_home.view.*
import leakcanary.AppWatcher
import me.xyj.conduct.demo.R
import me.xyj.conduct.demo.list.RecycleController
import me.xyj.conduct.demo.vm.base.ViewModelController
import me.xyj.conduct.demo.vm.camera.CameraController
import me.xyj.conduct.demo.vm.camera_v.CameraKitVController
import me.xyj.conduct.demo.vm.camerakit.CameraKitController
import me.xyj.conduct.demo.vm.camerax.CameraXController
import org.koin.core.KoinComponent
import org.koin.core.get

class HomeController : ViewModelController(), KoinComponent {


    private val vm by lazy {
        ViewModelProviders.of(activity as FragmentActivity).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        Log.d("HomeController", "onCreateView $vm")

        lifecycle.addObserver(vm)
        val view = inflater.inflate(R.layout.controller_home, container, false)
        view.tv_second.setOnClickListener {
            router.pushController(
                RouterTransaction.with(SecondController()).pushChangeHandler(
                    get<HorizontalChangeHandler>()
                )
                    .popChangeHandler(get<FadeChangeHandler>())
            )
        }

        view.tv_recycle.setOnClickListener {
            router.pushController(
                RouterTransaction.with(RecycleController()).pushChangeHandler(
                    get<HorizontalChangeHandler>()
                ).pushChangeHandler(
                    get<HorizontalChangeHandler>()
                )
            )
        }

        view.tv_camera.setOnClickListener {
            router.pushController(
                RouterTransaction.with(CameraController()).pushChangeHandler(
                    get<HorizontalChangeHandler>()
                ).pushChangeHandler(
                    get<HorizontalChangeHandler>()
                )
            )
        }

        view.tv_camerax.setOnClickListener {
            router.pushController(
                RouterTransaction.with(CameraXController()).pushChangeHandler(
                    get<HorizontalChangeHandler>()
                ).pushChangeHandler(
                    get<HorizontalChangeHandler>()
                )
            )
        }

        view.tv_camerakit.setOnClickListener {
            router.pushController(
                RouterTransaction.with(CameraKitController()).pushChangeHandler(
                    get<HorizontalChangeHandler>()
                ).pushChangeHandler(
                    get<HorizontalChangeHandler>()
                )
            )
        }

        view.tv_camerakit_v.setOnClickListener {
            router.pushController(
                RouterTransaction.with(CameraKitVController()).pushChangeHandler(
                    get<HorizontalChangeHandler>()
                ).pushChangeHandler(
                    get<HorizontalChangeHandler>()
                )
            )
        }
        return view
    }


    override fun onAttach(view: View) {
        super.onAttach(view)
    }

    override fun onDestroy() {
        Log.d("HomeController", "onDestroy   $vm")
        super.onDestroy()
        AppWatcher.objectWatcher.watch(this)
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