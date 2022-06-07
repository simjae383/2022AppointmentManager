package com.sim981.a2022appointmentmanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.databinding.FragmentSettingsBinding

class SettingsFragment : BaseFragment() {
    lateinit var binding : FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
        setValues()
    }
    override fun setupEvents() {
//        프로필 이미지 변경 이벤트
        binding.settingMyProfileImg.setOnClickListener {

        }
//        닉네임 변경 이벤트
        binding.changeMyNickLayout.setOnClickListener {

        }
//        비밀번호 변경 이벤트
        binding.changePwLayout.setOnClickListener {

        }
//        외출 준비 시간 변경 이벤트
        binding.readyTimeLayout.setOnClickListener {

        }
//        로그아웃 이벤트
        binding.logoutLayout.setOnClickListener {

        }
//        회원 탈퇴 이벤트
        binding.secessionLayout.setOnClickListener {

        }

    }

    override fun setValues() {

    }
}