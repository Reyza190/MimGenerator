package com.example.mimgenerator.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mimgenerator.data.Resource
import com.example.mimgenerator.databinding.FragmentHomeBinding
import com.example.mimgenerator.ui.MemeAdapter
import com.example.mimgenerator.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel: HomeViewModel by viewModels()
    private val binding get() = _binding!!
    private val memeAdapter = MemeAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.srlRefresh.setOnRefreshListener {
            if (Utils.isInternetAvailable(requireContext())) {
                loadData()
            } else {
                Toast.makeText(requireContext(), "Internet Not Available", Toast.LENGTH_SHORT)
                    .show()
                binding.srlRefresh.isRefreshing = false
            }
        }
        memeAdapter.setOnItemClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailFragment(it)
            )
        }
        setRecycleView(memeAdapter)
        loadData()

    }

    private fun loadData() {
        homeViewModel.meme.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    Log.e("error", it.message.toString())
                    binding.srlRefresh.isRefreshing = false
                }

                is Resource.Loading -> binding.pbLoading.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    Log.e("data", it.toString())
                    memeAdapter.setData(it.data!!)
                    binding.srlRefresh.isRefreshing = false
                }
            }
        }
    }

    private fun setRecycleView(memeAdapter: MemeAdapter) {
        with(binding.rvMeme) {
            layoutManager = GridLayoutManager(context, 3)
            setHasFixedSize(true)
            adapter = memeAdapter
        }
    }

}