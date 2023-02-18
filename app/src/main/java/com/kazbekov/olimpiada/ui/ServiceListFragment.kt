package com.kazbekov.olimpiada.ui

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kazbekov.olimpiada.MainActivity
import com.kazbekov.olimpiada.MainViewModel
import com.kazbekov.olimpiada.R
import com.kazbekov.olimpiada.data.ServiceAdapter
import com.kazbekov.olimpiada.databinding.FragmentServiceListBinding

class ServiceListFragment : Fragment(R.layout.fragment_service_list) {
    private var _binding: FragmentServiceListBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private var listAdapter: ServiceAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentServiceListBinding.bind(view)

        initList()
        getServices()
        observeLiveData()
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
        listAdapter = null
    }

    private fun observeLiveData() {
        viewModel.services.observe(viewLifecycleOwner) {
            listAdapter?.submitList(it)
        }
        viewModel.downloadFailed.observe(viewLifecycleOwner) {
            //Не удалось загрузить сервсиы (например, когда нет интернета)
            //Диалоговое окно отобразится через некоторое время
            val action =
                ServiceListFragmentDirections.actionServiceListFragmentToWarningDialogFragment(
                    MainActivity.WARNING_TYPE_1
                )
            findNavController().navigate(action)
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(KEY_RETRY)
            ?.observe(viewLifecycleOwner) {
                if (it) {
                    getServices()
                }
            }
    }

    private fun initList() {
        listAdapter = ServiceAdapter { invokeOnItemSelected(it) }
        with(binding.servicesList) {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun getServices() {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            viewModel.getVKServices()
        } else {
            val action =
                ServiceListFragmentDirections.actionServiceListFragmentToWarningDialogFragment(
                    MainActivity.WARNING_TYPE_2
                )
            findNavController().navigate(action)
        }

    }

    private fun invokeOnItemSelected(position: Int) {
        val service = viewModel.services.value!![position]
        val action =
            ServiceListFragmentDirections.actionServiceListFragmentToServiceDetailFragment(service)
        findNavController().navigate(action)
    }

    companion object {
        const val KEY_RETRY = "retry"
    }
}