package com.mmfinfotech.streameApp.dashBoard.streme.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.PointF
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.baseActivity.DashBoardBaseActivity
import com.mmfinfotech.streameApp.util.*
import com.mmfinfotech.streameApp.utils.AppConstants
import com.otaliastudios.cameraview.*
import com.otaliastudios.cameraview.controls.Mode
import com.otaliastudios.cameraview.controls.PictureFormat
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.util.*

class CameraActivity : DashBoardBaseActivity() {
    private val TAG: String? = CameraActivity::class.java.simpleName
    private var camera: CameraView? = null
    private var imageFilePath: String? = null

    companion object {
//        const val ACTION_CLIP = "action_clip"
//        const val ACTION_POST = "action_post"
        fun getInstance(context: Context?) = Intent(context, CameraActivity::class.java).apply {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        camera = findViewById(R.id.camera)
//      camera?.setLifecycleOwner(this)
//      camera?.addCameraListener(Listener())
        imgButtonOk.visibility = View.GONE
        if (checkHasPermission(
                        this@CameraActivity,
                        Manifest.permission.CAMERA
                ) == true &&
                checkHasPermission(
                        this@CameraActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == true &&
                checkHasPermission(
                        this@CameraActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) == true
        ) {
            camera?.setLifecycleOwner(this)
            camera?.addCameraListener(Listener())
            camera?.mode = Mode.PICTURE;

        } else {
            requestPermissions(
                    arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    AppConstants.Permission.multiplePermissions
            )
        }
        setListners()
    }

    private fun setListners() {
        imageViewClose.setOnClickListener {
            onBackPressed()
        }
        imageViewCapture?.setOnClickListener { camera?.takePicture() }
        imageViewRetake.setOnClickListener {
            camera?.open()
        }
        imgButtonOk.setOnClickListener {
            startActivity(MyPostActivity.getInstance(this@CameraActivity, imageFilePath))
            finish()
        }
        imageViewImages.setOnClickListener {
            camera?.close()
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, AppConstants.RequestCode.imagePicker)
        }

        imageViewRetake?.setOnClickListener { camera?.toggleFacing() }
        imageViewFlash.setOnClickListener {
            var index: Int? = appPreferences?.getFlashSetting(this@CameraActivity)
            index = if ((index ?: 0) > 3) 0 else index?.inc()
            appPreferences?.setFlashSetting(this@CameraActivity, index)
            camera?.flash = getFlash(index) ?: defaultFlash
            setFlashImageResource(appPreferences?.getFlashSetting(this@CameraActivity))
        }
    }

    override fun onRequestPermissionsResult(requestCode : Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            AppConstants.Permission.multiplePermissions -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) &&
                    (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED) &&
                    (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                ) {
                    camera?.setLifecycleOwner(this)
                    camera?.addCameraListener(Listener())
                    camera?.open()
                }
                return
            }
        }
    }

    fun setFlashImageResource(index: Int?) {
        when (index) {
            0 -> imageViewFlash.setImageResource(R.drawable.ic_flash_off)
            1 -> imageViewFlash.setImageResource(R.drawable.ic_flash_on)
            2 -> imageViewFlash.setImageResource(R.drawable.ic_flash_auto)
            3 -> imageViewFlash.setImageResource(R.drawable.ic_flash_torch)
            3 -> imageViewFlash.setImageResource(R.drawable.ic_flash_off)
            else -> imageViewFlash.setImageResource(R.drawable.ic_flesh_light)
        }
    }

    private inner class Listener : CameraListener() {
        override fun onCameraOpened(options: CameraOptions) {
        }

        override fun onCameraError(exception: CameraException) {
            super.onCameraError(exception)
            when (exception.reason) {
                /**Unknown error. No further info available.*/
                CameraException.REASON_UNKNOWN ->
                    message(getString(R.string.camera_exception_reason_unknown))
                /**We failed to connect to the camera service.The camera might be in use by another app.*/
                CameraException.REASON_FAILED_TO_CONNECT ->
                    message(getString(R.string.camera_exception_reason_failed_connect))
//                    message("We failed to connect to the camera service.The camera might be in use by another app.")
                /**Failed to start the camera preview.Again, the camera might be in use by another app.*/
                CameraException.REASON_FAILED_TO_START_PREVIEW ->
                    message(getString(R.string.camera_exception_reason_start_preview))
//                    message("Failed to start the camera preview.Again, the camera might be in use by another app.")
                /**Camera was forced to disconnect.In Camera1, this is thrown when android.hardware.Camera.CAMERA_ERROR_EVICTED is caught.*/
                CameraException.REASON_DISCONNECTED ->
                    message(getString(R.string.camera_exception_reason_disconnected))
//                    message("Camera was forced to disconnect.In Camera1, this is thrown when android.hardware.Camera.CAMERA_ERROR_EVICTED is caught.")
                /**Could not take a picture or a picture snapshot,for some not specified reason.*/
                CameraException.REASON_PICTURE_FAILED ->
                    message(getString(R.string.camera_exception_reason_picture_failed))
//                    message("Could not take a picture or a picture snapshot,for some not specified reason.")
                /**Could not take a video or a video snapshot,for some not specified reason.*/
                CameraException.REASON_VIDEO_FAILED ->
                    message(getString(R.string.camera_exception_reason_video_failed))
//                    message("Could not take a video or a video snapshot,for some not specified reason.")
                /**Indicates that we could not find a camera for the current {@link Facing} value. This can be solved by changing the facing value and starting again.*/
                CameraException.REASON_NO_CAMERA ->
                    message(getString(R.string.camera_exception_reason_no_camera))
//                    message("Indicates that we could not find a camera for the current {@link Facing} value. This can be solved by changing the facing value and starting again.")
                else ->
                    message(getString(R.string.got_camera_exception) + exception.reason)
            }
        }

        override fun onPictureTaken(result: PictureResult) {
            super.onPictureTaken(result)
            val extension: String = when (result.format) {
                PictureFormat.JPEG -> "jpg"
                PictureFormat.DNG -> "dng"
                else -> throw RuntimeException("Unknown format.")
            }
            val file: File? = File(getAppFolder(), "image_${UUID.randomUUID()}.$extension")
            CameraUtils.writeToFile(result.data, (file)!!) { file ->
                if (file != null) {
                    imageFilePath = file.path
                } else {
                    Toast.makeText(
                            this@CameraActivity,
                            getString(R.string.error_while_writing_file),
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }


            imageViewCapture.visibility = View.GONE
            imgButtonOk.visibility = View.VISIBLE
            camera?.close()
        }

        override fun onExposureCorrectionChanged(
                newValue: Float,
                bounds: FloatArray,
                fingers: Array<PointF>?
        ) {
            super.onExposureCorrectionChanged(newValue, bounds, fingers)
            message("Exposure correction:$newValue")
        }

        override fun onZoomChanged(
                newValue: Float,
                bounds: FloatArray,
                fingers: Array<PointF>?
        ) {
            super.onZoomChanged(newValue, bounds, fingers)
            message("Zoom:$newValue")
        }
    }


    fun message(@NonNull content: String?) {
        Toast.makeText(this, content, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstants.RequestCode.imagePicker && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    val contentURI = data.data
                    val sImageUri = contentURI
                    val file = File(getRealPathFromURI(this@CameraActivity, contentURI))
                    val imageFilePath = file.absolutePath
                    startActivity(MyPostActivity.getInstance(this@CameraActivity, imageFilePath))
                    finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        imgButtonOk.visibility = View.GONE
        imageViewCapture.visibility = View.VISIBLE
    }
}