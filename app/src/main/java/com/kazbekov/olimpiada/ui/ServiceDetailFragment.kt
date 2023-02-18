package com.kazbekov.olimpiada.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.kazbekov.olimpiada.R
import com.kazbekov.olimpiada.databinding.FragmentServiceDetailBinding

class ServiceDetailFragment : Fragment(R.layout.fragment_service_detail) {
    private var _binding: FragmentServiceDetailBinding? = null
    private val binding
        get() = _binding!!
    private val args: ServiceDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentServiceDetailBinding.bind(view)

        initToolbar()
        bindServiceInfo()
    }

    override fun onStart() {
        super.onStart()

        binding.openService.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(args.service.serviceUrl))
            startActivity(browserIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun initToolbar() {
        binding.detailToolbar.title = args.service.name
        binding.detailToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bindServiceInfo() {
        with(args.service) {
            binding.serviceName.text = name
            binding.serviceDescription.text = description

            Glide.with(binding.root)
                .load(iconUrl)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.serviceIcon)
        }
    }
}