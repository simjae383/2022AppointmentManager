package com.sim981.a2022appointmentmanager.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.adapters.MyFriendsSpinnerAdapter
import com.sim981.a2022appointmentmanager.adapters.StartPlaceSpinnerAdapter
import com.sim981.a2022appointmentmanager.databinding.ActivityEditAppointmentBinding
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.models.PlaceData
import com.sim981.a2022appointmentmanager.models.UserData
import com.sim981.a2022appointmentmanager.utils.SizeUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditAppointmentActivity : BaseActivity() {
    lateinit var binding : ActivityEditAppointmentBinding

//    선택한 약속 일시를 저장할 멤버변수
    val mSelectedDateTime = Calendar.getInstance() //기본값 : 현재시간

//    출발 장소를 담고 있는 관련 변수
    var mStartPlaceList = ArrayList<PlaceData>()
    lateinit var mStartPlaceSpinnerAdapter : StartPlaceSpinnerAdapter
    lateinit var mSelectedStartPlace : PlaceData

//    친구 목록을 담고 있는 관련 변수
    var mFriendsList = ArrayList<UserData>()
    lateinit var mFriendsSpinnerAdapter : MyFriendsSpinnerAdapter
    var mSelectedFriendsList = ArrayList<UserData>()

//    네이버 지도 관련 변수
    var mNaverMap : NaverMap? = null
    var mStartPlaceMarker = Marker()
    var mSelectedPlaceMarker = Marker()
    var mSelectedLatLng : LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_appointment)
        titleTxt.text = "약속 등록하기"
        addAppointmentBtn.visibility = View.GONE
        setupEvents()
        setValues()
    }

    override fun onResume() {
        super.onResume()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setupEvents() {
//      날짜선택
        binding.dateTxt.setOnClickListener {
//            날짜를 선택하고 할 일(인터페이스) 작성
            val dl = object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
                    mSelectedDateTime.set(year, month, day)

                    val sdf = SimpleDateFormat("yyyy. M. d")
                    Log.d("선택된 시간", sdf.format(mSelectedDateTime.time))

                    binding.dateTxt.text = sdf.format(mSelectedDateTime.time)
                }
            }
//            DatePickerDialog 팝업
            DatePickerDialog(mContext, dl, mSelectedDateTime.get(Calendar.YEAR),
                mSelectedDateTime.get(Calendar.MONTH),mSelectedDateTime.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

//        시간 선택
        binding.timeTxt.setOnClickListener {
            val tsl = object : TimePickerDialog.OnTimeSetListener{
                override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
                    mSelectedDateTime.set(Calendar.HOUR_OF_DAY, hour)
                    mSelectedDateTime.set(Calendar.MINUTE, minute)

                    val sdf = SimpleDateFormat("a h:mm")
                    binding.timeTxt.text = sdf.format(mSelectedDateTime.time)
                }
            }
            TimePickerDialog(mContext, tsl, mSelectedDateTime.get(Calendar. HOUR_OF_DAY),
            mSelectedDateTime.get(Calendar.MINUTE), false).show()
        }

//        시작장소 스피너 선택 이벤트
        binding.startPlaceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                mSelectedStartPlace = mStartPlaceList[position]

                mNaverMap?.let {
                    mStartPlaceMarker.position = LatLng(mSelectedStartPlace.latitude, mSelectedStartPlace.longitude)
                    mStartPlaceMarker.map = mNaverMap

                    val cameraUpdate = CameraUpdate.scrollTo(LatLng(mSelectedStartPlace.latitude, mSelectedStartPlace.longitude))
                    it.moveCamera(cameraUpdate)
                }
            }
        }

//        친구 초대 버튼 클릭 이벤트 처리
        binding.invitedFriendBtn.setOnClickListener {
            val selectedFriend = mFriendsList[binding.invitedFriendSpinner.selectedItemPosition]

            if (mSelectedFriendsList.contains(selectedFriend)){
                Toast.makeText(mContext, "이미 추가한 친구입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textView = TextView(mContext)
            textView.setBackgroundResource(R.drawable.lightgray_rectangle_r6)

            textView.setPadding(SizeUtil.dpToPx(mContext, 5f).toInt())

            textView.text = selectedFriend.nickName

            textView.setOnClickListener {
                binding.friendListLayout.removeView(textView)
                mSelectedFriendsList.remove(selectedFriend)

                if (mSelectedFriendsList.size == 0) {
                    binding.friendListLayout.visibility = View.GONE
                }
            }

            binding.friendListLayout.visibility = View.VISIBLE
            binding.friendListLayout.addView(textView)
            mSelectedFriendsList.add(selectedFriend)
        }

//        약속 추가 이벤트
        binding.addBtn.setOnClickListener {
//            약속 제목 확인
            val inputTitle = binding.titleEdt.text.toString()
            if (inputTitle.isBlank()) {
                Toast.makeText(mContext, "약속 제목을 정해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
//            날짜/시간 선택 확인
            if (binding.dateTxt.text == "일자 선택") {
                Toast.makeText(mContext, "약속 일자를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.timeTxt.text == "시간 선택") {
                Toast.makeText(mContext, "약속 시간을 선택하지 않았습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
//            지금 시간과 선택된 시간과의 시간차 계산
            if(mSelectedDateTime.timeInMillis < Calendar.getInstance().timeInMillis){
                Toast.makeText(mContext, "현재 시간 이후의 시간으로 선택해 주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
//            도착 지점 선택 여부
            if (mSelectedLatLng == null){
                Toast.makeText(mContext, "도착지를 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
//            장소명 기록 여부
            val inputPlaceName = binding.placeNameEdt.text.toString()
            if (inputPlaceName.isBlank()) {
                Toast.makeText(mContext, "약속 장소명을 기입해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

//            선택한 친구 목록 => "1, 3, 4 " 가공해서 첨부
            var friendListStr = ""

//            friendListStr에 들어갈 String을 선택된 친구목록을 이용해 가공
            for (friend in mSelectedFriendsList) {
                friendListStr += friend.id
                friendListStr += ","
            }

//            마지막 ", " 제거 => 글자가 0보다 커야 가능
            if (friendListStr != "") {
                friendListStr = friendListStr.substring(0, friendListStr.length -1)
            }

//            서버에서 요구한 약속일시 양식대로 변환하여 전달
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
//            서버에 내 시간정보(Date)를 올릴때도, UTC로 변환하여 통신
            sdf.timeZone = TimeZone.getTimeZone("UTC")

            apiList.postRequestAddAppointment(inputTitle, sdf.format(mSelectedDateTime.time),
            mSelectedStartPlace.name, mSelectedStartPlace.latitude, mSelectedStartPlace.longitude,
            inputPlaceName, mSelectedLatLng!!.latitude, mSelectedLatLng!!.longitude, friendListStr)
                .enqueue(object :Callback<BasicResponse>{
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if(response.isSuccessful) {
                            Toast.makeText(mContext, "약속이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                    }

                })
        }

//        지도 영역에 손을 대면 스크롤뷰 정지 - 텍스트뷰를 겹쳐두고 텍스트뷰 터치시 정지
        binding.scrollHelpTxt.setOnTouchListener { view, motionEvent ->
            binding.scrollView.requestDisallowInterceptTouchEvent(true)
            return@setOnTouchListener false
        }
    }

    override fun setValues() {
        mStartPlaceSpinnerAdapter = StartPlaceSpinnerAdapter(mContext, R.layout.list_place_item, mStartPlaceList)
        binding.startPlaceSpinner.adapter = mStartPlaceSpinnerAdapter

        mFriendsSpinnerAdapter = MyFriendsSpinnerAdapter(mContext, R.layout.list_user_item, mFriendsList)
        binding.invitedFriendSpinner.adapter = mFriendsSpinnerAdapter

        getMyPlaceListFromServer()
        getMyFriendsListFromServer()

        binding.mapView.getMapAsync {
            if(mNaverMap == null){
                mNaverMap = it
            }
            mSelectedPlaceMarker.icon = OverlayImage.fromResource(com.naver.maps.map.R.drawable.navermap_default_marker_icon_red)

            it.setOnMapClickListener { pointF, latLng ->
                mSelectedLatLng = latLng

                mSelectedPlaceMarker.position = latLng
                mSelectedPlaceMarker.map = it
            }
        }
    }

    fun getMyPlaceListFromServer () {
        apiList.getRequestMyPlace().enqueue(object : Callback<BasicResponse>{
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    mStartPlaceList.clear()
                    mStartPlaceList.addAll(response.body()!!.data.places)
                    mStartPlaceSpinnerAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    fun getMyFriendsListFromServer() {
        apiList.getRequestMyFriendsList("my").enqueue(object : Callback<BasicResponse>{
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful)
                    mFriendsList.clear()
                    mFriendsList.addAll(response.body()!!.data.friends)
                    mFriendsSpinnerAdapter.notifyDataSetChanged()
            }
        })
    }
}