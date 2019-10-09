package me.xyj.conduct.demo.vm.camera_v


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Facing
import kotlinx.android.synthetic.main.controller_camerakit_v.view.*
import me.xyj.conduct.demo.vm.base.ViewModelController
import me.xyj.conduct.demo.vm.camera.PIC_FILE_NAME
import java.io.File


class CameraKitVController : ViewModelController() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
        inflater.inflate(
            me.xyj.conduct.demo.R.layout.controller_camerakit_v, null
        ).apply {
            view_finder.setLifecycleOwner(this@CameraKitVController)

            view_finder.facing = Facing.BACK

            camera_switch_button.setOnClickListener {
                view_finder.facing =
                    if (view_finder.facing == Facing.BACK) Facing.FRONT else Facing.BACK

            }
            view_finder.addCameraListener(object : CameraListener() {
                override fun onPictureTaken(result: PictureResult) {
                    super.onPictureTaken(result)
                    val file = File(requireActivity().getExternalFilesDir(null), PIC_FILE_NAME)
                    result.toFile(file) {
                        Toast.makeText(requireActivity(), "save ok!", Toast.LENGTH_SHORT).show()
                    }
                }
            })

            camera_capture_button.setOnClickListener {
                if (view_finder.isTakingPicture) {
                    return@setOnClickListener
                }
                view_finder.takePicture()
            }
        }


    override fun onDestroyView(view: View) {

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

    }
}