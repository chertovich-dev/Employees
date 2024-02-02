package com.dikiyserge.employees.view

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.dikiyserge.employees.databinding.FragmentSortingDialogBinding
import com.dikiyserge.employees.getParcelableData
import com.dikiyserge.employees.model.Repository
import com.dikiyserge.employees.viewmodel.MainViewModel
import com.dikiyserge.employees.viewmodel.MainViewModelFactory
import com.dikiyserge.employees.viewmodel.SortType

const val ARG_SORT_TYPE = "sort_type"

class SortingListDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentSortingDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(Repository(requireActivity().applicationContext))
    }

    private fun setSorting(sortType: SortType) {
        viewModel.sorting = sortType
        dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSortingDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sortType: SortType? = arguments?.getParcelableData(ARG_SORT_TYPE)

        if (sortType != null) {
            when (sortType) {
                SortType.ALPHABET -> binding.radioButtonAlphabet.isChecked = true
                SortType.BIRTHDAY -> binding.radioButtonBirthday.isChecked = true
            }
        }

        binding.radioButtonAlphabet.setOnClickListener {
            setSorting(SortType.ALPHABET)
        }

        binding.radioButtonBirthday.setOnClickListener {
            setSorting(SortType.BIRTHDAY)
        }
    }

    companion object {
        fun newInstance(sortType: SortType): SortingListDialogFragment =
            SortingListDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_SORT_TYPE, sortType)
                }
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}