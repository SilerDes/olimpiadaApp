package com.kazbekov.olimpiada.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kazbekov.olimpiada.MainActivity
import com.kazbekov.olimpiada.R
import com.kazbekov.olimpiada.databinding.DialogFragmentWarningBinding
import com.kazbekov.olimpiada.databinding.WarningDownloadFailedBinding
import com.kazbekov.olimpiada.databinding.WarningExternalStorageBinding

class WarningDialogFragment : DialogFragment(R.layout.dialog_fragment_warning) {
    private var _binding: DialogFragmentWarningBinding? = null
    private val binding
        get() = _binding!!
    private val args: WarningDialogFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = DialogFragmentWarningBinding.bind(view)

        when (args.warningType) {
            MainActivity.WARNING_TYPE_1 -> {
                WarningDownloadFailedBinding.inflate(
                    LayoutInflater.from(requireContext()),
                    binding.root,
                    true
                ).also {
                    it.retry.setOnClickListener {
                        findNavController().previousBackStackEntry?.savedStateHandle?.set(
                            ServiceListFragment.KEY_RETRY,
                            true
                        )
                        findNavController().popBackStack()
                    }
                }
            }
            MainActivity.WARNING_TYPE_2 -> {
                WarningExternalStorageBinding.inflate(
                    LayoutInflater.from(requireContext()),
                    binding.root,
                    true
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}