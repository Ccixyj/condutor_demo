package me.xyj.conduct.demo.vm.camerakit


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.camerakit.CameraKit
import com.camerakit.CameraKit.FACING_BACK
import com.camerakit.CameraKitView
import kotlinx.android.synthetic.main.controller_camerakit.view.*
import me.xyj.conduct.demo.vm.base.ViewModelController
import me.xyj.conduct.demo.vm.camera.PIC_FILE_NAME
import java.io.File
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit


class CameraKitController : ViewModelController() {
    var face = FACING_BACK

    private val cameraOpenCloseLock = Semaphore(1)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
        inflater.inflate(
            me.xyj.conduct.demo.R.layout.controller_camerakit, null
        ).apply {
            kotlin.runCatching {
                cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)
            }.onFailure {
                Log.e("CameraKit", "error Acquire :$it")
            }
            view_finder.cameraListener = object : CameraKitView.CameraListener {
                override fun onOpened() {
                    Log.e("CameraKit", "onOpened")
                    cameraOpenCloseLock.release()
                }

                override fun onClosed() {
                    Log.e("CameraKit", "onClosed")
                    cameraOpenCloseLock.release()
                }
            }
            view_finder.facing = FACING_BACK

            camera_switch_button.setOnClickListener {
                view_finder.facing =
                    if (face == FACING_BACK) CameraKit.FACING_FRONT else FACING_BACK
                face = view_finder.facing
            }

            camera_capture_button.setOnClickListener {
                view_finder.captureImage { cameraKitView, bytes ->
                    kotlin.runCatching {
                        val file = File(requireActivity().getExternalFilesDir(null), PIC_FILE_NAME)
                        file.outputStream().use {
                            it.write(bytes)
                        }
                        Toast.makeText(
                            requireActivity(),
                            "ok ${file.absolutePath}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }


    override fun onAttach(view: View) {
        super.onAttach(view)

        view.view_finder.onStart()
        println("CameraKit onAttach")
        view.view_finder.onResume()
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        println("CameraKit onDetach")
        view.view_finder.onPause()
        view.view_finder.onStop()

    }

    override fun onDestroyView(view: View) {
        view.view_finder.removeCameraListener()
        view.view_finder.removeErrorListener()
        view.view_finder.removePreviewListener()
        super.onDestroyView(view)

    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        view?.view_finder?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}