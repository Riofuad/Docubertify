package com.riofuad.identitycardclassificationapp.ui.activities

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.riofuad.identitycardclassificationapp.ui.viewmodels.ResultViewModel
import com.riofuad.identitycardclassificationapp.databinding.ActivityResultBinding
import com.riofuad.identitycardclassificationapp.utils.Helpers

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private val viewModel: ResultViewModel by viewModels()
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle back button click
        binding.btnBack.setOnClickListener { finish() }

        /// Load image from URI
        val imageUri = intent.getStringExtra("image_uri")?.let { Uri.parse(it) }
        bitmap = Helpers.loadImageFromUri(this, imageUri)
        binding.ivPreview.setImageBitmap(bitmap)

        setupTextRecognition()
        observeViewModel()
    }

    private fun setupTextRecognition() {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        binding.btnProcess.setOnClickListener {
            bitmap?.let { bmp ->
                val inputImage = InputImage.fromBitmap(bmp, 0)
                recognizer.process(inputImage)
                    .addOnSuccessListener { result ->
                        viewModel.sendTextToApi(result.text)
                    }
                    .addOnFailureListener { e ->
                        Log.e("TextRecognition", e.toString())
                    }
            } ?: Log.e("TextRecognition", "Bitmap is null, cannot process text.")
        }
    }

    private fun observeViewModel() {
        viewModel.predictResponse.observe(this) { predictResponse ->
            // Use Helpers to show the bottom sheet
            Helpers.showBottomSheet(supportFragmentManager, predictResponse)
        }

        viewModel.apiError.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }

        viewModel.loading.observe(this) { isLoading ->
            binding.loadingLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnProcess.isEnabled = !isLoading
            binding.btnBack.isEnabled = !isLoading
            if (isLoading) startLoadingAnimation() else stopLoadingAnimation()
        }
    }

    private fun startLoadingAnimation() {
        val animator = ObjectAnimator.ofFloat(binding.loadingText, "alpha", 1f, 0f)
        animator.duration = 1000
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }

    private fun stopLoadingAnimation() {
        binding.loadingText.clearAnimation()
        binding.loadingText.alpha = 1f
    }

//    private fun showBottomSheet(predictResponse: PredictResponse) {
//        val fragment = when (predictResponse.prediction) {
//            "KTP" -> {
//                val ktpBottomSheetFragment = KtpBottomSheetFragment()
//                ktpBottomSheetFragment.arguments = Bundle().apply {
//                    putBundle("data", Bundle().apply {
//                        putString("document_type", "KTP")
//                        putString("nik", predictResponse.geminiResponse.data["nik"])
//                        putString("nama", predictResponse.geminiResponse.data["nama"])
//                        putString("tempat_tanggal_lahir", predictResponse.geminiResponse.data["tempat_tanggal_lahir"])
//                        putString("jenis_kelamin", predictResponse.geminiResponse.data["jenis_kelamin"])
//                        putString("gol_darah", predictResponse.geminiResponse.data["gol_darah"])
//                        putString("alamat", predictResponse.geminiResponse.data["alamat"])
//                        putString("rt_rw", predictResponse.geminiResponse.data["rt_rw"])
//                        putString("kel_desa", predictResponse.geminiResponse.data["kel_desa"])
//                        putString("kecamatan", predictResponse.geminiResponse.data["kecamatan"])
//                        putString("agama", predictResponse.geminiResponse.data["agama"])
//                        putString("status_perkawinan", predictResponse.geminiResponse.data["status_perkawinan"])
//                        putString("pekerjaan", predictResponse.geminiResponse.data["pekerjaan"])
//                        putString("kewarganegaraan", predictResponse.geminiResponse.data["kewarganegaraan"])
//                        putString("berlaku_hingga", predictResponse.geminiResponse.data["berlaku_hingga"])
//                    })
//                }
//                ktpBottomSheetFragment
//            }
//
//            "SIM" -> {
//                val simBottomSheetFragment = SimBottomSheetFragment()
//                simBottomSheetFragment.arguments = Bundle().apply {
//                    putBundle("data", Bundle().apply {
//                        putString("document_type", "SIM")
//                        putString("no_lisensi", predictResponse.geminiResponse.data["no_lisensi"])
//                        putString("kelas_lisensi", predictResponse.geminiResponse.data["kelas_lisensi"])
//                        putString("nama", predictResponse.geminiResponse.data["nama"])
//                        putString("tempat_tanggal_lahir", predictResponse.geminiResponse.data["tempat_tanggal_lahir"])
//                        putString("gol_darah", predictResponse.geminiResponse.data["gol_darah"])
//                        putString("jenis_kelamin", predictResponse.geminiResponse.data["jenis_kelamin"])
//                        putString("alamat", predictResponse.geminiResponse.data["alamat"])
//                        putString("pekerjaan", predictResponse.geminiResponse.data["pekerjaan"])
//                        putString("domisili", predictResponse.geminiResponse.data["domisili"])
//                        putString("berlaku_hingga", predictResponse.geminiResponse.data["berlaku_hingga"])
//                    })
//                }
//                simBottomSheetFragment
//            }
//
//            else -> {
//                Toast.makeText(
//                    this@ResultActivity,
//                    "Unknown document type: ${predictResponse.prediction}",
//                    Toast.LENGTH_SHORT
//                ).show()
//                null
//            }
//        }
//
//        fragment?.show(supportFragmentManager, "BottomSheetFragment")
//    }
}


