package com.sim981.a2022appointmentmanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.databinding.FragmentFriendsBinding
import com.sim981.a2022appointmentmanager.ui.MainActivity

class FriendsFragment : BaseFragment() {
    lateinit var binding : FragmentFriendsBinding

    lateinit var addBtn : ImageView
    lateinit var requestBtn : ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addBtn = (mContext as MainActivity).firstBtn
        requestBtn = (mContext as MainActivity).secondBtn
        setupEvents()
        setValues()
    }
    override fun setupEvents() {

    }

    override fun setValues() {
        addBtn.visibility = View.VISIBLE
        requestBtn.visibility = View.VISIBLE

        addBtn.setImageResource(R.drawable.baseline_add_black_24dp)
        requestBtn.setImageResource(R.drawable.baseline_person_add_black_24dp)
    }
}