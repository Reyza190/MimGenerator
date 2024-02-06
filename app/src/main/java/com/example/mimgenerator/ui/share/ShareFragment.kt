package com.example.mimgenerator.ui.share

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.mimgenerator.databinding.FragmentShareBinding
import com.example.mimgenerator.ui.detail.SharedViewModel
import com.example.mimgenerator.utils.Utils


class ShareFragment : Fragment() {
    private var _binding: FragmentShareBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentShareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        sharedViewModel.bitmap.observe(viewLifecycleOwner) { bitmap ->
            binding.ivResult.setImageBitmap(bitmap)
            // Simpan bitmap ke storage dan dapatkan Uri
            val imageUri = Utils.saveImageToExternalStorage(requireContext(), bitmap, "$bitmap.jpg")
            if (imageUri != null) {
                binding.btnFb.setOnClickListener {
                    shareImageToSocialMedia(requireContext(), imageUri, "com.facebook.katana")
                }
                binding.btnTwitter.setOnClickListener {
                    shareImageToSocialMedia(requireContext(), imageUri, "com.twitter.android")
                }
            } else {
                // Handle error, gagal menyimpan gambar atau mendapatkan Uri
            }
        }

    }

    private fun shareImageToSocialMedia(context: Context, imageUri: Uri, packageName: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            `package` = packageName
        }
        try {
            context.startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            // Tampilkan pesan error atau alternatif lain jika aplikasi tidak ditemukan
        }
    }
}