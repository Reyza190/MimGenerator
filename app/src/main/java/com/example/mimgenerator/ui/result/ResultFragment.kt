package com.example.mimgenerator.ui.result

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mimgenerator.data.Resource
import com.example.mimgenerator.databinding.FragmentResultBinding
import com.example.mimgenerator.ui.detail.SharedViewModel
import com.example.mimgenerator.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.bitmap.observe(viewLifecycleOwner) { bitmap ->
            Log.d("sk", bitmap.toString())
            binding.ivResult.setImageBitmap(bitmap)
            binding.btnSave.setOnClickListener {
                sharedViewModel.downloadAndSaveImage(
                    bitmap,
                    title = Utils.randomString(4),
                    description = Utils.randomString(9)
                )
            }
        }
        sharedViewModel.downloadStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Resource.Loading -> {
                    // Tampilkan indikator loading
                }

                is Resource.Success -> {
                    // Proses selesai, tampilkan pesan sukses atau update UI
                    Toast.makeText(context, "Download berhasil: ${status.data}", Toast.LENGTH_LONG)
                        .show()
                }

                is Resource.Error -> {
                    // Tampilkan pesan error
                    Toast.makeText(context, "Error: ${status.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
        binding.apply {
            btnShare.setOnClickListener {
                findNavController().navigate(
                    ResultFragmentDirections.actionResultFragmentToShareFragment()
                )
            }
            ivBack.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

    }


}