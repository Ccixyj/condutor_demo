package me.xyj.conduct.demo.vm.camerax

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.*
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.controller_camera_new.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.xyj.conduct.demo.R
import me.xyj.conduct.demo.vm.base.ViewModelController
import me.xyj.conduct.demo.vm.camera.ImageSaver
import me.xyj.conduct.demo.vm.camera.PIC_FILE_NAME
import java.io.File

class CameraXController : ViewModelController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
        inflater.inflate(
            R.layout.controller_camera_new, null
        ).apply {

            viewFinder = findViewById(R.id.texture)

            // Request camera permissions
            if (allPermissionsGranted()) {
                viewFinder.post {  }
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }

            // Every time the provided texture view changes, recompute layout
            viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                updateTransform()
            }

        }


    // This is an arbitrary number we are using to keep tab of the permission
    // request. Where an app has multiple context for requesting permission,
    // this can help differentiate the different contexts
    private val REQUEST_CODE_PERMISSIONS = 10

    // This is an array of all the permission specified in the manifest
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)


    // Add this after onCreate

    private lateinit var viewFinder: TextureView

    override fun onAttach(view: View) {
        super.onAttach(view)
        if(view.texture.isAvailable){
            return
        }
       kotlin.runCatching {
           startCamera()
       }
    }

    private fun startCamera() {

        // Create configuration object for the viewfinder use case
        val previewConfig = PreviewConfig.Builder().apply {
            setTargetAspectRatio(Rational(1, 1))
            setTargetResolution(Size(640, 640))
            setLensFacing(CameraX.LensFacing.FRONT)
        }.build()

        // Build the viewfinder use case
        val preview = Preview(previewConfig)

        // Every time the viewfinder is updated, recompute layout
        preview.setOnPreviewOutputUpdateListener {
            viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform()
        }

        // Create configuration object for the image capture use case
        val imageCaptureConfig = ImageCaptureConfig.Builder().apply {
            setTargetAspectRatio(Rational(1, 1))
            // We don't set a resolution for image capture; instead, we
            // select a capture mode which will infer the appropriate
            // resolution based on aspect ration and requested mode
            setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
            setLensFacing(CameraX.LensFacing.FRONT)

        }.build()

        // Build the image capture use case and attach button click listener
        val imageCapture = ImageCapture(imageCaptureConfig)
        view?.max?.setOnClickListener {

            val file = File(requireActivity().getExternalFilesDir(null), PIC_FILE_NAME)
            imageCapture.takePicture(file, object :ImageCapture.OnImageSavedListener{

                override fun onImageSaved(file: File) {
                    val msg = "Photo capture succeeded: ${file.absolutePath}"
                    Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
                    Log.d("CameraXApp", msg)

                }

                override fun onError(
                    imageCaptureError: ImageCapture.ImageCaptureError,
                    message: String,
                    cause: Throwable?
                ) {
                    val msg = "Photo capture failed: $message"
                    Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
                    Log.e("CameraXApp", msg)
                }
            })
        }

        view?.picture?.setOnClickListener {

            val file = File(requireActivity().getExternalFilesDir(null), PIC_FILE_NAME)
            imageCapture.takePicture(object :ImageCapture.OnImageCapturedListener(){
                override fun onCaptureSuccess(image: ImageProxy?, rotationDegrees: Int) {
                    val msg = "Photo capture succeeded: ${file.absolutePath} -> $rotationDegrees"
                    Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
                    Log.d("CameraXApp", msg)
                    image?.image?.let {
                        launch {
                            Dispatchers.IO.dispatch(this.coroutineContext,ImageSaver(it , file))
                        }

                    }

                }

                override fun onError(
                    imageCaptureError: ImageCapture.ImageCaptureError,
                    message: String,
                    cause: Throwable?
                ) {
                    super.onError(imageCaptureError, message, cause)
                    val msg = "Photo capture failed: $message"
                    Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
                    Log.e("CameraXApp", msg)
                }
            })
        }

        // Setup image analysis pipeline that computes average pixel luminance
        val analyzerConfig = ImageAnalysisConfig.Builder().apply {

            // Use a worker thread for image analysis to prevent glitches
            val analyzerThread = HandlerThread(
                "LuminosityAnalysis"
            ).apply { start() }

            setCallbackHandler(Handler(analyzerThread.looper))
            // In our analysis, we care more about the latest image than
            // analyzing *every* image
            setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)

        }.build()

        // Build the image analysis use case and instantiate our analyzer
        val analyzerUseCase = ImageAnalysis(analyzerConfig).apply {
            analyzer = LuminosityAnalyzer()
        }

        // Bind use cases to lifecycle
        // If Android Studio complains about "this" being not a LifecycleOwner
        // try rebuilding the project or updating the appcompat dependency to
        // version 1.1.0 or higher.
        CameraX.unbindAll()
        CameraX.bindToLifecycle(this ,imageCapture)
    }

    private fun updateTransform() {

        val matrix = Matrix()

        // Compute the center of the view finder
        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f

        // Correct preview output to account for display rotation
        val rotationDegrees = when (viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }

        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        // Finally, apply transformations to our TextureView
        viewFinder.setTransform(matrix)
    }

    /**
     * Process result from permission request dialog box, has the request
     * been granted? If yes, start Camera. Otherwise display a toast
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                viewFinder.post { startCamera() }
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                router.popController(this)
            }
        }
    }

    /**
     * Check if all permission specified in the manifest have been granted
     */
    private fun allPermissionsGranted(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }
}