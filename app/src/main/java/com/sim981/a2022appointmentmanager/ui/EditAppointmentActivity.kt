package com.sim981.a2022appointmentmanager.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.sim981.a2022appointmentmanager.R
import com.sim981.a2022appointmentmanager.adapters.MyFriendsSpinnerAdapter
import com.sim981.a2022appointmentmanager.adapters.StartPlaceSpinnerAdapter
import com.sim981.a2022appointmentmanager.databinding.ActivityEditAppointmentBinding
import com.sim981.a2022appointmentmanager.models.AppointmentData
import com.sim981.a2022appointmentmanager.models.BasicResponse
import com.sim981.a2022appointmentmanager.models.PlaceData
import com.sim981.a2022appointmentmanager.models.UserData
import com.sim981.a2022appointmentmanager.utils.SizeUtil
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class EditAppointmentActivity : BaseActivity() {
    lateinit var binding: ActivityEditAppointmentBinding

    //    선택한 약속 일시를 저장할 멤버변수
    var mSelectedDateTime = Calendar.getInstance() //기본값 : 현재시간

    //    출발 장소를 담고 있는 관련 변수
    var mStartPlaceList = ArrayList<PlaceData>()
    lateinit var mStartPlaceSpinnerAdapter: StartPlaceSpinnerAdapter
    lateinit var mSelectedStartPlace: PlaceData

    //    친구 목록을 담고 있는 관련 변수
    var mFriendsList = ArrayList<UserData>()
    lateinit var mFriendsSpinnerAdapter: MyFriendsSpinnerAdapter
    var mSelectedFriendsList = ArrayList<UserData>()

    //    네이버 지도 관련 변수
    var mNaverMap: NaverMap? = null
    var mStartPlaceMarker = Marker()
    var mSelectedPlaceMarker = Marker()
    var mSelectedLatLng: LatLng? = null

    //    데이터 추가/수정 여부 확인
    var isEditOk = false
    lateinit var mEditData: AppointmentData

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
//        새로 만드는 약속인지, 아니면 기존에 만든 약속의 수정인지 확인
//        기존에 만든 약속이면 기존의 입력값을 설정 값에 덮어 씌우기
        isEditOk = intent.getBooleanExtra("isEditData", false)
        getMyPlaceListFromServer(isEditOk)
        if (isEditOk) {
            titleTxt.text = "약속 수정하기"
            binding.addBtn.text = "약속 수정하기"
            mEditData = intent.getSerializableExtra("currentAppointmentData") as AppointmentData
            putEditData(mEditData)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setupEvents() {
//      날짜선택
        binding.dateTxt.setOnClickListener {
//            날짜를 선택하고 할 일(인터페이스) 작성
            val dl = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
                    mSelectedDateTime.set(year, month, day)

                    val sdf = SimpleDateFormat("yyyy. M. d")

                    binding.dateTxt.text = sdf.format(mSelectedDateTime.time)
                }
            }
//            DatePickerDialog 팝업
            DatePickerDialog(
                mContext, dl, mSelectedDateTime.get(Calendar.YEAR),
                mSelectedDateTime.get(Calendar.MONTH), mSelectedDateTime.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

//        시간 선택
        binding.timeTxt.setOnClickListener {
            val tsl = object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
                    mSelectedDateTime.set(Calendar.HOUR_OF_DAY, hour)
                    mSelectedDateTime.set(Calendar.MINUTE, minute)
                    val sdf = SimpleDateFormat("a h:mm")
                    binding.timeTxt.text = sdf.format(mSelectedDateTime.time)
                }
            }
            TimePickerDialog(
                mContext, tsl, mSelectedDateTime.get(Calendar.HOUR_OF_DAY),
                mSelectedDateTime.get(Calendar.MINUTE), false
            ).show()
        }

//        시작장소 스피너 선택 이벤트
        binding.startPlaceSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    mSelectedStartPlace = mStartPlaceList[position]
                    mNaverMap?.let {
                        mStartPlaceMarker.position =
                            LatLng(mSelectedStartPlace.latitude, mSelectedStartPlace.longitude)
                        mStartPlaceMarker.map = mNaverMap

                        val cameraUpdate = CameraUpdate.scrollTo(
                            LatLng(
                                mSelectedStartPlace.latitude, mSelectedStartPlace.longitude
                            )
                        )
                        it.moveCamera(cameraUpdate)
                    }
                }
            }

//        친구 초대 버튼 클릭 이벤트 처리
        binding.invitedFriendBtn.setOnClickListener {
            val selectedFriend = mFriendsList[binding.invitedFriendSpinner.selectedItemPosition]

            if (mSelectedFriendsList.contains(selectedFriend)) {
                Toast.makeText(mContext, "이미 추가한 친구입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            editFriendListItem(selectedFriend)
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
            if (mSelectedDateTime.timeInMillis < Calendar.getInstance().timeInMillis) {
                Toast.makeText(mContext, "현재 시간 이후의 시간으로 선택해 주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
//            도착 지점 선택 여부
            if (mSelectedLatLng == null) {
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
//            friendListStr에 들어갈 String을 선택된 친구 목록을 이용해 가공
            for (friend in mSelectedFriendsList) {
                friendListStr += friend.id
                friendListStr += ","
            }

//            마지막 ", " 제거 => 글자가 0보다 커야 가능
            if (friendListStr != "") {
                friendListStr = friendListStr.substring(0, friendListStr.length - 1)
            }

//            서버에서 요구한 약속일시 양식대로 변환하여 전달
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
//            서버에 내 시간정보(Date)를 올릴때도, UTC로 변환하여 통신
            sdf.timeZone = TimeZone.getTimeZone("UTC")

//                등록 기능일 경우 실행할 등록 API
            if (!isEditOk) {
                apiList.postRequestAddAppointment(
                    inputTitle,
                    sdf.format(mSelectedDateTime.time),
                    mSelectedStartPlace.name,
                    mSelectedStartPlace.latitude,
                    mSelectedStartPlace.longitude,
                    inputPlaceName,
                    mSelectedLatLng!!.latitude,
                    mSelectedLatLng!!.longitude,
                    friendListStr
                )
                    .enqueue(object : Callback<BasicResponse> {
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(mContext, "약속이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                        }
                    })
            } else {
//                수정 기능일 경우 실행할 수정 API
                apiList.putRequestEditAppointment(
                    mEditData.id,
                    inputTitle,
                    sdf.format(mSelectedDateTime.time),
                    mSelectedStartPlace.name,
                    mSelectedStartPlace.latitude,
                    mSelectedStartPlace.longitude,
                    inputPlaceName,
                    mSelectedLatLng!!.latitude,
                    mSelectedLatLng!!.longitude,
                    friendListStr
                )
                    .enqueue(object : Callback<BasicResponse> {
                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                        }

                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(mContext, "약속이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                                finish()
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

//        지도 영역에 손을 대면 스크롤뷰 정지 - 텍스트뷰를 겹쳐두고 텍스트뷰 터치시 정지
        binding.scrollHelpTxt.setOnTouchListener { view, motionEvent ->
            binding.scrollView.requestDisallowInterceptTouchEvent(true)
            return@setOnTouchListener false
        }
    }

    override fun setValues() {
//        스피너 할당
        mStartPlaceSpinnerAdapter =
            StartPlaceSpinnerAdapter(mContext, R.layout.list_place_item, mStartPlaceList)
        binding.startPlaceSpinner.adapter = mStartPlaceSpinnerAdapter

        mFriendsSpinnerAdapter =
            MyFriendsSpinnerAdapter(mContext, R.layout.list_user_item, mFriendsList)
        binding.invitedFriendSpinner.adapter = mFriendsSpinnerAdapter

//        장소와 친구 목록 가져와 대기
        getMyPlaceListFromServer(isEditOk)
        getMyFriendsListFromServer()
//네이버 맵 기능 - 화면 터치시 빨간 마커와 함께 도착 장소 좌표 할당
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.editMapView) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.editMapView, it).commit()
            }
        mapFragment.getMapAsync {
            mNaverMap = it

            mSelectedPlaceMarker.icon =
                OverlayImage.fromResource(com.naver.maps.map.R.drawable.navermap_default_marker_icon_red)

            it.setOnMapClickListener { pointF, latLng ->
                mSelectedLatLng = latLng

                mSelectedPlaceMarker.position = latLng
                mSelectedPlaceMarker.map = it
            }
        }
    }

    // 기존 데이터를 설정 화면과 값에 할당
    fun putEditData(mEditData: AppointmentData) {
        binding.titleEdt.setText(mEditData.title)

        val sdf = SimpleDateFormat("yyyy. M. d")
        binding.dateTxt.text = sdf.format(mEditData.datetime.time)
        val sdf2 = SimpleDateFormat("a h:mm")
        binding.timeTxt.text = sdf2.format(mEditData.datetime.time)

        mSelectedDateTime.time = mEditData.datetime

        mSelectedFriendsList.clear()
        for (editFriend in mEditData.invitedFriends) {
            editFriendListItem(editFriend)

        }
        binding.placeNameEdt.setText(mEditData.place)
        mSelectedLatLng = LatLng(mEditData.latitude, mEditData.longitude)
    }

    //장소 목록을 불러오는 API
    fun getMyPlaceListFromServer(isListCleanOk: Boolean) {
        apiList.getRequestMyPlace().enqueue(object : Callback<BasicResponse> {
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {
                if (response.isSuccessful) {
                    mStartPlaceList.clear()
                    mStartPlaceList.addAll(response.body()!!.data.places)
                    mStartPlaceSpinnerAdapter.notifyDataSetChanged()
//                    기존 장소를 스피너가 미리 선택시키도록 하며 없을시 0번째 선택
                    if (isListCleanOk) {
                        for (startPlace in mStartPlaceList) {
                            if (mEditData.startPlace.equals(startPlace.name)) {
                                binding.startPlaceSpinner.setSelection(
                                    mStartPlaceList.indexOf(
                                        startPlace
                                    )
                                )
                                return
                            }
                        }
                        binding.startPlaceSpinner.setSelection(0)
                    }

                }
            }
        })
    }

    //친구 목록을 불러오는 API
    fun getMyFriendsListFromServer() {
        apiList.getRequestMyFriendsList("my").enqueue(object : Callback<BasicResponse> {
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {
                if (response.isSuccessful) {
                    mFriendsList.clear()
                    mFriendsList.addAll(response.body()!!.data.friends)
                    mFriendsSpinnerAdapter.notifyDataSetChanged()

                }
            }
        })
    }

    //입력받은 친구를 참여자 목록에 추가하고 화면에 표시, 표시된 텍스트뷰 클릭시 해당 친구 삭제
    fun editFriendListItem(addFriend: UserData) {
        val textView = TextView(mContext)
        textView.setBackgroundResource(R.drawable.lightskyblue_rectangle_r6)

        textView.setPadding(SizeUtil.dpToPx(mContext, 5f).toInt())

        textView.text = addFriend.nickName

        textView.setOnClickListener {
            if (textView.text.equals(mEditData.user.nickName)) {
//                해당 친구가 약속의 발의자일 경우 목록에서 지우지 않음 - 서버 규칙
                return@setOnClickListener
            }
            binding.friendListLayout.removeView(textView)
            mSelectedFriendsList.remove(addFriend)

            if (mSelectedFriendsList.size == 0) {
                binding.friendListLayout.visibility = View.GONE
            }
        }

        binding.friendListLayout.visibility = View.VISIBLE
        binding.friendListLayout.addView(textView)
        mSelectedFriendsList.add(addFriend)
    }
}