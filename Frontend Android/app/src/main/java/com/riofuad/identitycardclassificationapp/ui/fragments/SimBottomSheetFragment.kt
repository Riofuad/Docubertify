package com.riofuad.identitycardclassificationapp.ui.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.riofuad.identitycardclassificationapp.databinding.FragmentSimBottomSheetBinding
import com.riofuad.identitycardclassificationapp.utils.Helpers

class SimBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSimBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textInputLayouts = listOf(
            binding.tiSimLicenseNumber,
            binding.tiSimLicenseType,
            binding.tiSimName,
            binding.tiSimBirth,
            binding.tiSimBloodType,
            binding.tiSimGender,
            binding.tiSimAddress,
            binding.tiSimJob,
            binding.tiSimDomicile,
            binding.tiSimValidUntil
        )


        Helpers.setupBottomSheetBehavior(
            binding.root,
            binding.dragHandle,
            binding.tvClassifiedAs,
            requireContext()
        )
        setupCopy(requireContext(), textInputLayouts)
        setupCopyAll()
        setupSimData(textInputLayouts)
    }

    private fun setupSimData(textInputLayouts: List<TextInputLayout>) {
        val data = arguments?.getBundle("data")
        binding.tvClassifiedAs.text = "Surat Izin Mengemudi\n(SIM)"
        Helpers.populateSimData(data, textInputLayouts)
    }

    private fun setupCopy(context: Context, textInputLayouts: List<TextInputLayout>) {
        textInputLayouts.forEach {
            Helpers.setupCopyFunctionality(context, it)
        }
    }

    private fun setupCopyAll() {
        binding.btnSimCopyAll.setOnClickListener {
            val concatenatedText = buildString {
                append("Tipe SIM: ${binding.tiSimLicenseType.editText?.text.toString()}\n")
                append("No. Lisensi: ${binding.tiSimLicenseNumber.editText?.text.toString()}\n")
                append("Tempat\\Tgl Lahir: ${binding.tiSimBirth.editText?.text.toString()}\n")
                append("Gol. Darah: ${binding.tiSimBloodType.editText?.text.toString()}\n")
                append("Jenis Kelamin: ${binding.tiSimGender.editText?.text.toString()}\n")
                append("Alamat: ${binding.tiSimAddress.editText?.text.toString()}\n")
                append("Pekerjaan: ${binding.tiSimJob.editText?.text.toString()}\n")
                append("Domisili: ${binding.tiSimDomicile.editText?.text.toString()}\n")
                append("Berlaku Hingga: ${binding.tiSimValidUntil.editText?.text.toString()}")
            }

            if (concatenatedText.isNotEmpty()) {
                val clipboard =
                    requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", concatenatedText)
                clipboard.setPrimaryClip(clip)

                Toast.makeText(requireContext(), "All text copied to clipboard", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "No text to copy", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}