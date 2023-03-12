package cn.lancet.navigation.rest

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.ImageCapture.FLASH_MODE_AUTO
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import cn.lancet.navigation.databinding.ActivityPreviewCameraBinding
import com.google.common.util.concurrent.ListenableFuture
import java.io.File


class PreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewCameraBinding

    private var mUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initEvent()
    }

    private fun initEvent() {
        binding.ivBack.setOnClickListener { finish() }
        openCamera()
        binding.btnCapture.setOnClickListener {
            takePhoto()
        }
    }

    private fun openCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build().apply {
                    setSurfaceProvider(binding.preview.surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this@PreviewActivity,
                    cameraSelector, preview
                )
            } catch (exc: Exception) {

            }
        }, ContextCompat.getMainExecutor(this))

    }

    private fun takePhoto() {
        val imageCapture = ImageCapture.Builder()
            .setFlashMode(FLASH_MODE_AUTO)
            .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build()

        val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
            ProcessCameraProvider.getInstance(this)
        val processCameraProvider = cameraProviderFuture.get()
        processCameraProvider.bindToLifecycle(
            this,
            CameraSelector.DEFAULT_BACK_CAMERA, imageCapture
        )

        val outputFileOptions = ImageCapture.OutputFileOptions
            .Builder(File("$externalCacheDir" + File.separator + System.currentTimeMillis() + ".png"))
            .build()

        imageCapture.takePicture(outputFileOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    mUri = outputFileResults.savedUri
                    setResult(RESULT_OK, intent.setData(mUri))
                    finish()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("Lancet  ", "Photo capture failed: ${exception.message}", exception)
                }

            })
    }


}