package com.example.mimgenerator.ui.detail

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mimgenerator.databinding.FragmentDetailBinding
import com.example.mimgenerator.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private var uriImage: Uri? = null
    private var dX: Float = 0f
    private var dY: Float = 0f
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.

            if (uri != null) {
                binding.ivOverlay.setImageURI(uri)
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = DetailFragmentArgs.fromBundle(arguments as Bundle).data
        binding.apply {
            Glide.with(requireContext()).load(data.url).into(ivMemePicture)
            ivLogo.setOnClickListener {
                openGalery()
            }
            ivAddText.setOnClickListener {
                addEditableTextOverImage()
            }
            ivBack.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
            btnContinue.setOnClickListener {
                sharedViewModel.setBitmap(Utils.saveLayoutAsImage(binding.frameLayout))
                findNavController().navigate(
                    DetailFragmentDirections.actionDetailFragmentToResultFragment(
                        Utils.saveLayoutAsImage(binding.frameLayout)
                            .toString()
                    )
                )

            }
        }



        if (uriImage != null) {
            binding.ivOverlay.setImageURI(uriImage)
            Log.e("uri", uriImage.toString())
        }
        val imageOverlay = binding.ivOverlay
        val imageBackground = binding.ivMemePicture



        imageOverlay.setOnTouchListener { views, event ->
            gestureMoveItem(views, event, imageBackground)
        }


    }

    private fun openGalery() {


        // Launch the photo picker and let the user choose only images.
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun gestureMoveItem(
        view: View,
        event: MotionEvent,
        imageBackground: ImageView
    ): Boolean {

        val newX = event.rawX + dX
        val newY = event.rawY + dY


        val bgLeft = imageBackground.x - 40
        val bgRight = bgLeft + imageBackground.width + 80
        val bgTop = imageBackground.y
        val bgBottom = bgTop + imageBackground.height - 140


        val maxRight = bgRight - view.width
        val maxBottom = bgBottom - view.height

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dX = view.x - event.rawX
                dY = view.y - event.rawY
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val boundedX = newX.coerceAtLeast(bgLeft).coerceAtMost(maxRight)
                val boundedY = newY.coerceAtLeast(bgTop).coerceAtMost(maxBottom)

                view.animate()
                    .x(boundedX)
                    .y(boundedY)
                    .setDuration(0)
                    .start()
                return true
            }

            else -> return false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun addEditableTextOverImage() {
        val editText = EditText(requireContext()).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).also { params ->
                params.gravity = Gravity.CENTER // Atur sesuai kebutuhan
            }
            hint = "Type here..."
            // Tampilkan petunjuk
            setTextColor(Color.BLACK)
            imeOptions = EditorInfo.IME_ACTION_DONE
            background = null // Hilangkan background
            isCursorVisible = true // Tampilkan kursor
            // Sesuaikan styling lainnya jika diperlukan
        }

        binding.frameLayout.addView(editText)
        editText.requestFocus() // Fokus untuk langsung bisa mengetik

        // Munculkan keyboard secara otomatis
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)

        editText.setOnTouchListener { view, event ->
            gestureMoveItem(view, event, binding.ivMemePicture)
        }
    }

}