package com.riofuad.identitycardclassificationapp.utils

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputLayout
import com.riofuad.identitycardclassificationapp.R
import com.riofuad.identitycardclassificationapp.data.response.PredictResponse
import com.riofuad.identitycardclassificationapp.ui.activities.ResultActivity
import com.riofuad.identitycardclassificationapp.ui.fragments.KtpBottomSheetFragment
import com.riofuad.identitycardclassificationapp.ui.fragments.SimBottomSheetFragment
import java.io.File
import java.io.FileOutputStream

object Helpers {

    fun isPermissionGranted(context: Context): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    fun resizeBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val aspectRatio: Float = width.toFloat() / height.toFloat()
        var newWidth = 800
        var newHeight = (800 / aspectRatio).toInt()

        if (newHeight > 600) {
            newHeight = 600
            newWidth = (600 * aspectRatio).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val file = File(context.cacheDir, "temp_image.jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return Uri.fromFile(file)
    }

    fun openResultActivity(context: Context, image: Bitmap) {
        val file = File(context.cacheDir, "result_image.jpg")
        FileOutputStream(file).use { outputStream ->
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }

        val intent = Intent(context, ResultActivity::class.java)
        val imageUri = Uri.fromFile(file)
        intent.putExtra("image_uri", imageUri.toString())
        context.startActivity(intent)
    }

    fun loadImageFromUri(context: Context, imageUri: Uri?): Bitmap? {
        return try {
            val inputStream = imageUri?.let { context.contentResolver.openInputStream(it) }
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            bitmap
        } catch (e: Exception) {
            Log.e("Helpers", "Error loading image", e)
            null
        }
    }

    fun showBottomSheet(fragmentManager: FragmentManager, predictResponse: PredictResponse) {
        val fragment = when (predictResponse.prediction) {
            "KTP" -> createKtpBottomSheet(predictResponse)
            "SIM" -> createSimBottomSheet(predictResponse)
            else -> {
                Toast.makeText(
                    fragmentManager.findFragmentByTag("ResultActivity")?.context,
                    "Unknown document type: ${predictResponse.prediction}",
                    Toast.LENGTH_SHORT
                ).show()
                null
            }
        }
        fragment?.show(fragmentManager, "BottomSheetFragment")
    }

    private fun createKtpBottomSheet(predictResponse: PredictResponse): KtpBottomSheetFragment {
        return KtpBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putBundle("data", Bundle().apply {
                    predictResponse.geminiResponse.data.forEach { (key, value) ->
                        putString(key, value)
                    }
                })
            }
        }
    }

    private fun createSimBottomSheet(predictResponse: PredictResponse): SimBottomSheetFragment {
        return SimBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putBundle("data", Bundle().apply {
                    predictResponse.geminiResponse.data.forEach { (key, value) ->
                        putString(key, value)
                    }
                })
            }
        }
    }

    fun setupBottomSheetBehavior(
        view: View,
        dragHandle: View,
        titleTextView: TextView,
        context: Context
    ) {
        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)

        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val peekHeight = dragHandle.height + titleTextView.height
                bottomSheetBehavior.peekHeight = peekHeight
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                bottomSheetBehavior.isFitToContents = true
                bottomSheetBehavior.isDraggable = true
            }
        })
    }

    fun setupCopyFunctionality(context: Context, textInputLayout: TextInputLayout) {
        val textInputEditText = textInputLayout.editText

        // Initially hide the end icon
        textInputLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
        textInputLayout.setEndIconDrawable(R.drawable.ic_round_copy)

        // Add a TextWatcher to listen for changes in the text field
        textInputEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Show the end icon only if the text field is not empty
                textInputLayout.isEndIconVisible = !s.isNullOrEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Set up the copy functionality for the end icon
        textInputLayout.setEndIconOnClickListener {
            val textToCopy = textInputEditText?.text.toString()

            if (textToCopy.isNotEmpty()) {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", textToCopy)
                clipboard.setPrimaryClip(clip)

                Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "No text to copy", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun populateKtpData(data: Bundle?, textInputLayouts: List<TextInputLayout>) {
        textInputLayouts.forEach { textInputLayout ->
            when (textInputLayout) {
                textInputLayouts[0] -> textInputLayout.editText?.setText(data?.getString("nik"))
                textInputLayouts[1] -> textInputLayout.editText?.setText(data?.getString("nama"))
                textInputLayouts[2] -> textInputLayout.editText?.setText(data?.getString("tempat_tanggal_lahir"))
                textInputLayouts[3] -> textInputLayout.editText?.setText(data?.getString("jenis_kelamin"))
                textInputLayouts[4] -> textInputLayout.editText?.setText(data?.getString("gol_darah"))
                textInputLayouts[5] -> textInputLayout.editText?.setText(data?.getString("alamat"))
                textInputLayouts[6] -> textInputLayout.editText?.setText(data?.getString("rt_rw"))
                textInputLayouts[7] -> textInputLayout.editText?.setText(data?.getString("kel_desa"))
                textInputLayouts[8] -> textInputLayout.editText?.setText(data?.getString("kecamatan"))
                textInputLayouts[9] -> textInputLayout.editText?.setText(data?.getString("agama"))
                textInputLayouts[10] -> textInputLayout.editText?.setText(data?.getString("status_perkawinan"))
                textInputLayouts[11] -> textInputLayout.editText?.setText(data?.getString("pekerjaan"))
                textInputLayouts[12] -> textInputLayout.editText?.setText(data?.getString("kewarganegaraan"))
                textInputLayouts[13] -> textInputLayout.editText?.setText(data?.getString("berlaku_hingga"))
            }
        }
    }

    fun populateSimData(data: Bundle?, textInputLayouts: List<TextInputLayout>) {
        textInputLayouts.forEach { textInputLayout ->
            when (textInputLayout) {
                textInputLayouts[0] -> textInputLayout.editText?.setText(data?.getString("no_lisensi"))
                textInputLayouts[1] -> textInputLayout.editText?.setText(data?.getString("kelas_lisensi"))
                textInputLayouts[2] -> textInputLayout.editText?.setText(data?.getString("nama"))
                textInputLayouts[3] -> textInputLayout.editText?.setText(data?.getString("tempat_tanggal_lahir"))
                textInputLayouts[4] -> textInputLayout.editText?.setText(data?.getString("gol_darah"))
                textInputLayouts[5] -> textInputLayout.editText?.setText(data?.getString("jenis_kelamin"))
                textInputLayouts[6] -> textInputLayout.editText?.setText(data?.getString("alamat"))
                textInputLayouts[7] -> textInputLayout.editText?.setText(data?.getString("pekerjaan"))
                textInputLayouts[8] -> textInputLayout.editText?.setText(data?.getString("domisili"))
                textInputLayouts[9] -> textInputLayout.editText?.setText(data?.getString("berlaku_hingga"))
            }
        }
    }
}
