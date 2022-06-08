package com.sim981.a2022appointmentmanager.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.sim981.a2022appointmentmanager.ui.LoginActivity
import com.sim981.a2022appointmentmanager.ui.PasswordActivity
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.databinding.FragmentSettingsBinding
import com.sim981.a2022appointmentmanager.dialogs.CustomAlertDialog
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.ui.MainActivity
import com.sim981.a2022appointmentmanager.utils.ContextUtil
import com.sim981.a2022appointmentmanager.utils.GlobalData
import com.sim981.a2022appointmentmanager.utils.URIPathHelper
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

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

    override fun onResume() {
        super.onResume()
        (mContext as MainActivity).addPlaceBtn.visibility = View.GONE
        (mContext as MainActivity).addFriendBtn.visibility = View.GONE
        (mContext as MainActivity).requestFriendBtn.visibility = View.GONE
        (mContext as MainActivity).myLocationBtn.visibility = View.GONE
    }
    override fun setupEvents() {
//        프로필 이미지 변경 이벤트
        binding.settingMyProfileImg.setOnClickListener {
            val pl = object : PermissionListener {
                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

                }

                override fun onPermissionGranted() {
                    val myIntent = Intent()
                    myIntent.action = Intent.ACTION_PICK
                    myIntent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
                    //갤러리로 사진을 가지러 이동
                    startForResult.launch(myIntent)
                }
            }
            TedPermission.create().setPermissionListener(pl)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .setDeniedMessage("[설정] > [권한]에서 갤러리 권한을 열어주세요.")
                .check()
        }
//        닉네임/외출 시간 변경 이벤트
        val ocl = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val type = p0!!.tag.toString()

                val alert = CustomAlertDialog(mContext)
                alert.myDialog()

                when (type) {
                    "nickname" -> {
                        alert.binding.dialogTitleTxt.text = "닉네임 변경"
                        alert.binding.dialogContentEdt.hint = "변경할 닉네임 입력"
                        alert.binding.dialogContentEdt.inputType = InputType.TYPE_CLASS_TEXT
                    }
                    "ready_minute" -> {
                        alert.binding.dialogTitleTxt.text = "준비 시간 설정"
                        alert.binding.dialogContentEdt.hint = "외출 준비에 몇 분 걸리는지"
                        alert.binding.dialogContentEdt.inputType = InputType.TYPE_CLASS_NUMBER
                    }
                }
                alert.binding.dialogBodyTxt.visibility = View.GONE

                alert.binding.dialogPositiveBtn.setOnClickListener {
                    if (alert.binding.dialogContentEdt.text.toString().isBlank()) {
                        Toast.makeText(mContext, "아무것도 기입되지 않았습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        apiList.patchRequestUserEdit(
                            type,
                            alert.binding.dialogContentEdt.text.toString()
                        )
                            .enqueue(object : Callback<BasicResponse> {
                                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                                }

                                override fun onResponse(
                                    call: Call<BasicResponse>,
                                    response: Response<BasicResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        val br = response.body()!!

                                        GlobalData.loginUser = br.data.user
                                        setUserData()
                                        alert.dialog.dismiss()
                                    } else {
                                        val errorBodyStr = response.errorBody()!!.string()
                                        val jsonObj = JSONObject(errorBodyStr)
                                        val message = jsonObj.getString("message")

                                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            })
                    }
                }
                alert.binding.dialogNegativeBtn.setOnClickListener {
                    alert.dialog.dismiss()
                }
            }
        }
        binding.changeMyNickLayout.setOnClickListener(ocl)
        binding.readyTimeLayout.setOnClickListener(ocl)

//        비밀번호 변경 이벤트
        binding.changePwLayout.setOnClickListener {
            val myIntent = Intent(mContext, PasswordActivity::class.java)
            startActivity(myIntent)
        }

//        로그아웃 이벤트
        binding.logoutLayout.setOnClickListener {
            val alert = CustomAlertDialog(mContext)
            alert.myDialog()

            alert.binding.dialogTitleTxt.text = "로그아웃"
            alert.binding.dialogBodyTxt.text = "정말 로그아웃 하시겠습니까?"
            alert.binding.dialogContentEdt.visibility = View.GONE
            alert.binding.dialogPositiveBtn.setOnClickListener {
                ContextUtil.clear(mContext)
                GlobalData.loginUser = null

                val myIntent = Intent(mContext, LoginActivity::class.java)
                myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(myIntent)

                alert.dialog.dismiss()
            }
            alert.binding.dialogNegativeBtn.setOnClickListener {
                alert.dialog.dismiss()
            }
        }
//        회원 탈퇴 이벤트
        binding.secessionLayout.setOnClickListener {
            val alert = CustomAlertDialog(mContext)
            alert.myDialog()

            alert.binding.dialogTitleTxt.text = "회원 탈퇴"
            alert.binding.dialogBodyTxt.text = "정말 탈퇴하시겠습니까?"
            alert.binding.dialogContentEdt.hint = "동의 라고 입력하십시오"
            alert.binding.dialogContentEdt.visibility = View.VISIBLE
            alert.binding.dialogPositiveBtn.setOnClickListener {
                apiList.deleteRequestUserSecession(alert.binding.dialogContentEdt.text.toString())
                    .enqueue(object : Callback<BasicResponse>{
                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                        }

                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if(response.isSuccessful){
                                val br = response.body()!!

                                Toast.makeText(mContext, br.message, Toast.LENGTH_SHORT).show()
                                ContextUtil.clear(mContext)
                                GlobalData.loginUser = null

                                val myIntent = Intent(mContext, LoginActivity::class.java)
                                myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(myIntent)

                                alert.dialog.dismiss()
                            } else {
                                val errorBodyStr = response.errorBody()!!.string()
                                val jsonObj = JSONObject(errorBodyStr)
                                val message = jsonObj.getString("message")

                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
            }
            alert.binding.dialogNegativeBtn.setOnClickListener {
                alert.dialog.dismiss()
            }
        }

    }

    override fun setValues() {
        setUserData()

        when (GlobalData.loginUser!!.provider) {
            "kakao" -> {
                binding.settingSocialLoginImg.setImageResource(R.drawable.kakao_login_icon)
            }
            "facebook" -> {
//                binding.settingSocialLoginImg.setImageResource(R.drawable.facebook_login_icon)
            }
            else -> binding.settingSocialLoginImg.visibility = View.GONE
        }
    }
//수정된 회원정보 갱신
    fun setUserData(){
        Glide.with(mContext).load(GlobalData.loginUser!!.profileImg).into(binding.settingMyProfileImg)
        binding.settingNickNameTxt.text = GlobalData.loginUser!!.nickName

        binding.readyTimeTxt.text = "${GlobalData.loginUser!!.readyMinute}분"
    }
//    갤러리에서 사진 가져오기
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK) {
            val dataUri = it.data?.data

            val file = File(URIPathHelper().getPath(mContext, dataUri!!))

            val fileReqBody = RequestBody.create(MediaType.get("image/*"), file)
            val body = MultipartBody.Part.createFormData("profile_image", "myFile.jpg", fileReqBody)

            apiList.putRequestUserImage(body).enqueue(object : Callback<BasicResponse>{
                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){
                        GlobalData.loginUser = response.body()!!.data.user
                        Glide.with(mContext).load(GlobalData.loginUser!!.profileImg).into(binding.settingMyProfileImg)
                        Toast.makeText(mContext, "프로필 사진이 변경되었습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }
}