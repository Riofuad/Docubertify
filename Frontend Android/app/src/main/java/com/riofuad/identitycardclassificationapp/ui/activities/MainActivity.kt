package com.riofuad.identitycardclassificationapp.ui.activities

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.riofuad.identitycardclassificationapp.databinding.ActivityMainBinding
import com.riofuad.identitycardclassificationapp.ui.viewmodels.MainViewModel
import com.riofuad.identitycardclassificationapp.utils.Helpers

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.setupActivityResultLaunchers(this)

        binding.btnCamera.setOnClickListener {
            if (Helpers.isPermissionGranted(this)) {
                val uri = viewModel.getOutputUri(this)
                viewModel.cameraLauncher.launch(uri)
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.CAMERA), 100
                )
            }
        }

        binding.btnGallery.setOnClickListener {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            viewModel.galleryLauncher.launch(galleryIntent)
        }
    }
}

