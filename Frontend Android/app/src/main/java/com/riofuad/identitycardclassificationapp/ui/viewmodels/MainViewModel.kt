package com.riofuad.identitycardclassificationapp.ui.viewmodels

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.riofuad.identitycardclassificationapp.ui.activities.MainActivity
import com.riofuad.identitycardclassificationapp.utils.Helpers
import java.io.File

class MainViewModel : ViewModel() {

    lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var cropImageLauncher: ActivityResultLauncher<CropImageContractOptions>

    fun setupActivityResultLaunchers(activity: MainActivity) {
        cameraLauncher =
            activity.registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    val filePath = getOutputFilePath(activity)
                    val bitmap = BitmapFactory.decodeFile(filePath)
                    launchImageCropper(activity, Helpers.getImageUriFromBitmap(activity, bitmap))
                }
            }

        galleryLauncher =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    val selectedImageUri: Uri? = result.data?.data
                    selectedImageUri?.let { uri ->
                        val inputStream = activity.contentResolver.openInputStream(uri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        inputStream?.close()
                        val resizedBitmap = Helpers.resizeBitmap(bitmap)
                        launchImageCropper(
                            activity,
                            Helpers.getImageUriFromBitmap(activity, resizedBitmap)
                        )
                    }
                }
            }

        cropImageLauncher = activity.registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                val croppedImageUri = result.uriContent
                croppedImageUri?.let {
                    val inputStream = activity.contentResolver.openInputStream(it)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                    Helpers.openResultActivity(activity, bitmap)
                }
            } else {
                val exception = result.error
                exception?.printStackTrace()
            }
        }
    }

    private fun launchImageCropper(activity: MainActivity, uri: Uri) {
        cropImageLauncher.launch(
            CropImageContractOptions(
                uri = uri,
                cropImageOptions = CropImageOptions(
                    guidelines = CropImageView.Guidelines.ON,
                    outputCompressFormat = Bitmap.CompressFormat.JPEG,
                    outputCompressQuality = 100
                )
            )
        )
    }

    private fun getOutputFilePath(activity: MainActivity): String {
        return File(
            activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "temp_image.jpg"
        ).absolutePath
    }

    fun getOutputUri(activity: MainActivity): Uri {
        val file =
            File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temp_image.jpg")
        return FileProvider.getUriForFile(activity, "${activity.packageName}.fileprovider", file)
    }
}

