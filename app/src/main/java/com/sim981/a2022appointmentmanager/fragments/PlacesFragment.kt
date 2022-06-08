package com.sim981.a2022appointmentmanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.databinding.FragmentPlacesBinding
import com.sim981.a2022appointmentmanager.ui.MainActivity

class PlacesFragment : BaseFragment() {
    lateinit var binding : FragmentPlacesBinding

    lateinit var addBtn : ImageView
    lateinit var myLocationBtn : ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_places, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addBtn = (mContext as MainActivity).secondBtn
        myLocationBtn = (mContext as MainActivity).firstBtn
        setupEvents()
        setValues()
    }

    override fun onResume() {
        super.onResume()
        addBtn.visibility = View.VISIBLE
        myLocationBtn.visibility = View.VISIBLE

        addBtn.setImageResource(R.drawable.baseline_add_black_24dp)
        myLocationBtn.setImageResource(R.drawable.baseline_my_location_black_24dp)
    }
    override fun setupEvents() {

    }

    override fun setValues() {

    }
}