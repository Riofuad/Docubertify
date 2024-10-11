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
import com.riofuad.identitycardclassificationapp.databinding.FragmentKtpBottomSheetBinding
import com.riofuad.identitycardclassificationapp.utils.Helpers

class KtpBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentKtpBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKtpBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textInputLayouts = listOf(
            binding.tiKtpNik,
            binding.tiKtpName,
            binding.tiKtpBirth,
            binding.tiKtpGender,
            binding.tiKtpBloodType,
            binding.tiKtpAddress,
            binding.tiKtpRtRw,
            binding.tiKtpVillage,
            binding.tiKtpSubdistrict,
            binding.tiKtpReligion,
            binding.tiKtpMaritalStatus,
            binding.tiKtpJob,
            binding.tiKtpNationality,
            binding.tiKtpValidUntil
        )

        Helpers.setupBottomSheetBehavior(
            binding.root,
            binding.dragHandle,
            binding.tvClassifiedAs,
            requireContext()
        )
        setupCopy(requireContext(), textInputLayouts)
        setupCopyAll()
        setupKtpData(textInputLayouts)
    }

    private fun setupKtpData(textInputLayouts: List<TextInputLayout>) {
        val data = arguments?.getBundle("data")
        binding.tvClassifiedAs.text = "Kartu Tanda Penduduk\n(KTP)"
        Helpers.populateKtpData(data, textInputLayouts)
    }

    private fun setupCopy(context: Context, textInputLayouts: List<TextInputLayout>) {
        textInputLayouts.forEach {
            Helpers.setupCopyFunctionality(context, it)
        }
    }

    private fun setupCopyAll() {
        binding.btnKtpCopyAll.setOnClickListener {
            val concatenatedText = buildString {
                append("NIK: ${binding.tiKtpNik.editText?.text.toString()}\n")
                append("Nama: ${binding.tiKtpName.editText?.text.toString()}\n")
                append("Tempat\\Tgl Lahir: ${binding.tiKtpBirth.editText?.text.toString()}\n")
                append("Jenis Kelamin: ${binding.tiKtpGender.editText?.text.toString()}\n")
                append("Gol. Darah: ${binding.tiKtpBloodType.editText?.text.toString()}\n")
                append("Alamat: ${binding.tiKtpAddress.editText?.text.toString()}\n")
                append("RT\\RW: ${binding.tiKtpRtRw.editText?.text.toString()}\n")
                append("Kel\\Desa: ${binding.tiKtpVillage.editText?.text.toString()}\n")
                append("Kecamatan: ${binding.tiKtpSubdistrict.editText?.text.toString()}\n")
                append("Agama: ${binding.tiKtpReligion.editText?.text.toString()}\n")
                append("Status Perkawinan: ${binding.tiKtpMaritalStatus.editText?.text.toString()}\n")
                append("Pekerjaan: ${binding.tiKtpJob.editText?.text.toString()}\n")
                append("Kewarganegraan: ${binding.tiKtpNationality.editText?.text.toString()}\n")
                append("Berlaku Hingga: ${binding.tiKtpValidUntil.editText?.text.toString()}")
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
