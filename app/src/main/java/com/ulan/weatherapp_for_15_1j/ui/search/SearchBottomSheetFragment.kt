package com.ulan.weatherapp_for_15_1j.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ulan.weatherapp_for_15_1j.core.BaseBottomSheetFragment
import com.ulan.weatherapp_for_15_1j.databinding.FragmentSearchBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchBottomSheetFragment :  BaseBottomSheetFragment<FragmentSearchBottomSheetBinding>() {

    private lateinit var sendTextListner: SendText
    override fun getViewBinding(): FragmentSearchBottomSheetBinding {
        return FragmentSearchBottomSheetBinding.inflate(layoutInflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            sendTextListner = activity as SendText

        }catch (e:ClassCastException){
            e.message?.let { Log.e("dialog_fragment", it) }

        }
    }

    override fun initialize() {
        binding.btnSearch.setOnClickListener{
            val cityName = binding.etSearch.text.toString()
            sendTextListner.newText(cityName)
            dialog?.dismiss()

        }

    }
    interface SendText{
        fun newText(cityName:String){

        }

    }

}