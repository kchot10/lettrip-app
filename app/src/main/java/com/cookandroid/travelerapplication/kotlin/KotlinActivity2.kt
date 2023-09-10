package com.cookandroid.travelerapplication.kotlin

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.travelerapplication.R
import com.cookandroid.travelerapplication.mission.MissionTripActivity
import com.cookandroid.travelerapplication.task.InsertData_Place
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLng
import net.daum.mf.map.api.MapCircle
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class KotlinActivity2 : AppCompatActivity() {

    private val previousIndices = mutableListOf<Int>()
    private var currentIndex = 0
    private val ACCESS_FINE_LOCATION = 1000     // Request Code
    private val MIN_ARR_DIST = 500 // 최소 반경 500m (바깥)
    private val MAX_RADIUS = 1000 // 최대 반경 1km (이내)

    private lateinit var btnStart: Button
    private lateinit var btnRefresh: Button
    private lateinit var btnStop: Button

    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK 43a9d1617d8fb89af04db23790b3dd22"  // REST API 키
    }

    private val listItems = arrayListOf<ListLayout>()   // 리사이클러 뷰 아이템
    private val listAdapter = ListAdapter(listItems)    // 리사이클러 뷰 어댑터
    private var pageNumber = 1      // 검색 페이지 번호
    private var keyword = ""        // 검색 키워드
    private var latitude: Double = 1.0      // 검색 페이지 번호
    private var longitude: Double = 1.0      // 검색 페이지 번호
    private var mX: Double = 1.0
    private var mY: Double = 1.0
    private lateinit var item: ListLayout


    private lateinit var mapView: MapView
    private lateinit var rvList: RecyclerView
    private lateinit var etSearchField: EditText
    private lateinit var tvPageNumber: TextView
    private lateinit var textView_category: TextView
    private lateinit var btnSearch: Button
    private lateinit var btnPrevPage: Button
    private lateinit var btnNextPage: Button

    private var categoryText: String = ""
    val handler = Handler(Looper.getMainLooper())
    private var isTrackingStarted = true
    private var sec: Int = 0
    private var min: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin2)

        mapView = findViewById(R.id.mapView)
        rvList = findViewById(R.id.rv_list)
        btnSearch = findViewById(R.id.btn_search)
        btnPrevPage = findViewById(R.id.btn_prevPage)
        btnNextPage = findViewById(R.id.btn_nextPage)
        etSearchField = findViewById(R.id.et_search_field)
        tvPageNumber = findViewById(R.id.tv_pageNumber)
        textView_category = findViewById(R.id.textView_category)
        btnStart = findViewById(R.id.btn_start)
        btnRefresh = findViewById(R.id.button_refresh_mission)
        btnStop = findViewById(R.id.btn_stop)

        categoryText = generateRandomCategory()

        val builder = AlertDialog.Builder(this)

        // 리사이클러 뷰
        rvList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvList.adapter = listAdapter
        // 리스트 아이템 클릭 시 해당 위치로 이동
        listAdapter.setItemClickListener(object: ListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val mapPoint = MapPoint.mapPointWithGeoCoord(listItems[position].y, listItems[position].x)

                builder.setMessage("["+listItems[position].name+"]로 선택하시겠습니까?")
                builder.setPositiveButton("확인") { _, _ ->
                    intent.putExtra("name", listItems[position].name)
                    intent.putExtra("road", listItems[position].road)
                    intent.putExtra("address", listItems[position].address)
                    intent.putExtra("location_x", listItems[position].x.toString())
                    intent.putExtra("location_y", listItems[position].y.toString())
                    intent.putExtra("category_group_code", listItems[position].category_group_code)
                    intent.putExtra("category_group_name", listItems[position].category_group_name)
                    setResult(RESULT_OK, intent);
                    finish()
                }

                builder.setNegativeButton("취소") { _, _ ->
                    // Cancel 버튼을 눌렀을 때의 동작
                }

                builder.show()

                mapView.setMapCenterPointAndZoomLevel(mapPoint, 1, true)
            }
        })

        // 위치추적 버튼
        btnStart.setOnClickListener {
            if (isTrackingStarted) {
                stopTracking()
                btnStart.setText("계속하기")
            } else {
                startTracking()
                btnStart.setText("일시정지")
            }
            isTrackingStarted = !isTrackingStarted
        }

        btnRefresh.setOnClickListener {
            val itemSize = listItems.size
            val itemIndex = getRandomIndex(itemSize);
            if (itemIndex == -1){
                Toast.makeText(this, "더 이상 없습니다!", Toast.LENGTH_SHORT).show()
            }else{
                mapView.removeAllPOIItems() // 지도의 마커 모두 제거
                mapView.removeAllCircles()
                item = listItems.get(itemIndex)
                mX = item.x - longitude
                mY = item.y - latitude
                val circleCenter = MapPoint.mapPointWithGeoCoord(item.y, item.x)  // 원의 중심 좌표
                val radius = 50  // 원의 반지름
                val strokeColor = Color.RED  // 바깥 테두리 선 색상
                val fillColor = Color.parseColor("#F2DCE0")  // 주어진 색상으로 채우기 색상
                val fillAlpha = 64  // 채우기의 투명도 (0 ~ 255)
                val circle = MapCircle(circleCenter, radius, strokeColor, fillColor)
                circle.fillColor = fillColor and 0x00FFFFFF or (fillAlpha shl 24)  // 투명도를 적용한 채우기 색상
                mapView.addCircle(circle)

                // 지도에 마커 추가
                val point = MapPOIItem()
                point.apply {
                    itemName = item.name
                    mapPoint = MapPoint.mapPointWithGeoCoord(item.y,
                        item.x)
                    markerType = MapPOIItem.MarkerType.RedPin
                    selectedMarkerType = MapPOIItem.MarkerType.YellowPin
                }
                mapView.addPOIItem(point)
                mapView.selectPOIItem(point, true)
                val mapPoint = MapPoint.mapPointWithGeoCoord(item.y, item.x)
                mapView.setMapCenterPointAndZoomLevel(mapPoint, 2, true)
                mapView.setShowCurrentLocationMarker(true)
            }
        }

        // 추적중지 버튼
        btnStop.setOnClickListener {
            if (calculateDistance(mX, mY) > 100){
                val builder = AlertDialog.Builder(this)
                val distValue = calculateDistance(mX,mY).toInt() - 100;
                builder.setMessage("현재 "+distValue+"m 남았습니다. 미션을 포기하시겠습니까?")
                builder.setPositiveButton("확인") { _, _ ->
                    mapView.removeAllPOIItems()
                    mapView.removeAllCircles()
                    stopTracking()
                    intent.putExtra("success/fail", "fail")
                    setResult(RESULT_OK, intent);
                    finish()
                }
                builder.setNegativeButton("취소") { _, _ ->
                    // Cancel 버튼을 눌렀을 때의 동작
                }
                builder.show()
            }else{
                Toast.makeText(this, "미션 성공! 3초 뒤에 돌아갑니다.", Toast.LENGTH_LONG).show()
                mapView.removeAllPOIItems()
                mapView.removeAllCircles()
                stopTracking()
                handler.postDelayed({
                    intent.putExtra("success/fail", "success")
                    intent.putExtra("name", item.name)
                    intent.putExtra("road", item.road)
                    intent.putExtra("address", item.address)
                    intent.putExtra("location_x", item.x.toString())
                    intent.putExtra("location_y", item.y.toString())
                    intent.putExtra("category_group_code", item.category_group_code)
                    intent.putExtra("category_group_name", item.category_group_name)
                    setResult(RESULT_OK, intent);
                    finish()
                }, 3000L)
            }

        }

        // 검색 버튼
        btnSearch.setOnClickListener {
            if (categoryText.equals("FD6")){
                keyword = "음식점"
            }else if (categoryText.equals("CE7")){
                keyword = "카페"
            }
            textView_category.setText(keyword)
            pageNumber = 1
            searchKeyword(keyword, pageNumber)
        }

        // 이전 페이지 버튼
        btnPrevPage.setOnClickListener {
            pageNumber--
            tvPageNumber.text = pageNumber.toString()
            searchKeyword(keyword, pageNumber)
        }

        // 다음 페이지 버튼
        btnNextPage.setOnClickListener {
            pageNumber++
            tvPageNumber.text = pageNumber.toString()
            searchKeyword(keyword, pageNumber)
        }

        handler.postDelayed({
            fuctionStart()
        }, 1000L)

    }

    // 키워드 검색 함수
    private fun searchKeyword(keyword: String, page: Int) {
        val retrofit = Retrofit.Builder()          // Retrofit 구성
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        // 위치 권한을 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // 위치 권한이 허용된 경우 위치 관련 코드 실행
            val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val userNowLocation: Location? = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (userNowLocation != null) {
                latitude = userNowLocation.latitude  // x 좌표
                longitude = userNowLocation.longitude  // y 좌표ㄴ

            } else {
                // 위치 정보를 가져올 수 없는 경우 처리할 작업 수행
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
            // 위치 권한을 사용자에게 요청
        }

        val api = retrofit.create(KakaoAPI2::class.java)            // 통신 인터페이스를 객체로 생성
        val call = api.getSearchKeyword(API_KEY, keyword, longitude.toString(),latitude.toString(), MAX_RADIUS, categoryText, page)    // 검색 조건 입력

        // API 서버에 요청
        call.enqueue(object: Callback<ResultSearchKeyword> {
            override fun onResponse(call: Call<ResultSearchKeyword>, response: Response<ResultSearchKeyword>) {
                // 통신 성공
                addItemsAndMarkers(response.body())
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                // 통신 실패
                Log.w("LocalSearch", "통신 실패: ${t.message}")
            }
        })
    }

    // 검색 결과 처리 함수
    private fun addItemsAndMarkers(searchResult: ResultSearchKeyword?) {
        if (!searchResult?.documents.isNullOrEmpty()) {
            // 검색 결과 있음
            listItems.clear()                   // 리스트 초기화
            mapView.removeAllPOIItems() // 지도의 마커 모두 제거
            for (document in searchResult!!.documents) {
                val mY = document.y.toDouble() - latitude
                val mX = document.x.toDouble() - longitude
                if (calculateDistance(mX, mY) > MIN_ARR_DIST){
                    // 결과를 리사이클러 뷰에 추가
                    val item = ListLayout(document.place_name,
                        document.road_address_name,
                        document.address_name,
                        document.x.toDouble(),
                        document.y.toDouble(),
                        document.category_group_code,
                        document.category_group_name)
                    listItems.add(item)
                }
            }
            listAdapter.notifyDataSetChanged()

            btnNextPage.isEnabled = !searchResult.meta.is_end // 페이지가 더 있을 경우 다음 버튼 활성화
            btnPrevPage.isEnabled = pageNumber != 1             // 1페이지가 아닐 경우 이전 버튼 활성화

        } else {
            // 검색 결과 없음
            Toast.makeText(this, "검색 결과가 없습니다", Toast.LENGTH_SHORT).show()
        }
    }

    // 위치 권한 확인
    private fun permissionCheck() {
        val preference = getPreferences(MODE_PRIVATE)
        val isFirstCheck = preference.getBoolean("isFirstPermissionCheck", true)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 상태
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 권한 거절 (다시 한 번 물어봄)
                val builder = AlertDialog.Builder(this)
                builder.setMessage("현재 위치를 확인하시려면 위치 권한을 허용해주세요.")
                builder.setPositiveButton("확인") { dialog, which ->
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
                }
                builder.setNegativeButton("취소") { dialog, which ->

                }
                builder.show()
            } else {
                if (isFirstCheck) {
                    // 최초 권한 요청
                    preference.edit().putBoolean("isFirstPermissionCheck", false).apply()
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
                } else {
                    // 다시 묻지 않음 클릭 (앱 정보 화면으로 이동)
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("현재 위치를 확인하시려면 설정에서 위치 권한을 허용해주세요.")
                    builder.setPositiveButton("설정으로 이동") { dialog, which ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
                        startActivity(intent)
                    }
                    builder.setNegativeButton("취소") { dialog, which ->

                    }
                    builder.show()
                }
            }
        } else {
            // 권한이 있는 상태
            startTracking()
        }
    }

    // 권한 요청 후 행동
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한 요청 후 승인됨 (추적 시작)
                Toast.makeText(this, "위치 권한이 승인되었습니다", Toast.LENGTH_SHORT).show()
                startTracking()
            } else {
                // 권한 요청 후 거절됨 (다시 요청 or 토스트)
                Toast.makeText(this, "위치 권한이 거절되었습니다", Toast.LENGTH_SHORT).show()
                permissionCheck()
            }
        }
    }

    // GPS가 켜져있는지 확인
    private fun checkLocationService(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    // 위치추적 시작
    private fun startTracking() {
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
    }

    // 위치추적 중지
    private fun stopTracking() {
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
    }

    private fun fuctionStart() {
        if (checkLocationService()) {
            // GPS가 켜져있을 경우
            permissionCheck()
            btnSearch.performClick()
            handler.postDelayed({
                btnRefresh.performClick()
                timerRun()
            }, 2000L)

        } else {
            // GPS가 꺼져있을 경우
            Toast.makeText(this, "GPS를 켜주세요", Toast.LENGTH_SHORT).show()
        }
    }

    fun generateRandomCategory(): String {
        val random = Random()
        return if (random.nextBoolean()) {
            "FD6"
        } else {
            "CE7"
        }
    }

    fun calculateDistance(mY: Double, mX: Double): Double {
        val earthRadius = 6371.0 // 지구 반지름 (단위: km)

        val dLat = Math.toRadians(mY)
        val dLon = Math.toRadians(mX)

        val lat1 = 0.0 // 현재 위치의 위도 (단위: °)
        val lon1 = 0.0 // 현재 위치의 경도 (단위: °)

        val lat2 = lat1 + Math.toDegrees(dLat)
        val lon2 = lon1 + Math.toDegrees(dLon)

        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val distance = earthRadius * c

        return distance*1000
    }

    fun getRandomIndex(itemSize: Int): Int {
        val random = Random()
        var randomIndex = currentIndex

        if (previousIndices.size < itemSize) {
            while (previousIndices.contains(randomIndex)) {
                randomIndex = random.nextInt(itemSize)
            }
            previousIndices.add(randomIndex)
        } else {
            randomIndex = -1 // 이후 호출에서 중복된 값이 없다는 신호로 -1을 반환
        }

        currentIndex++
        return randomIndex
    }

    private fun timerRun() {
        // 타이머 구현
        val minTextView: TextView = findViewById(R.id.min)
        val secTextView: TextView = findViewById(R.id.sec)

        minTextView.text = "30"
        secTextView.text = "0"

        min = minTextView.text.toString().toInt()
        sec = secTextView.text.toString().toInt()

        val timer = Timer()
        val timerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (sec != 0) {
                        sec--
                        secTextView.text = sec.toString()
                    } else if (min != 0) {
                        sec = 60
                        sec--
                        min--
                        secTextView.text = sec.toString()
                        minTextView.text = min.toString()
                    }

                    if (min <= 9) {
                        minTextView.text = "0$min"
                    } else {
                        minTextView.text = min.toString()
                    }

                    if (sec <= 9) {
                        secTextView.text = "0$sec"
                    } else {
                        secTextView.text = sec.toString()
                    }

                    if (min == 0 && sec == 0) {
                        timer.cancel()
                        mapView.removeAllCircles()
                        mapView.removeAllPOIItems()
                        stopTracking()
                        showAlertDialog()
                    }

                    if (sec <= 30 && min <= 0) {
                        secTextView.setTextColor(Color.parseColor("#E12E23")) // 빨간색으로 바꾸기
                        minTextView.setTextColor(Color.parseColor("#E12E23")) // 빨간색으로 바꾸기
                    }
                }
            }
        }

        timer.schedule(timerTask, 0, 1000) // 타이머 스케줄링 추가
    }

    private fun showAlertDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("미션에 실패하셨습니다. 다시 한번 도전하시겠습니까?")

        builder.setPositiveButton("YES") { dialogInterface, i ->
            recreate()
        }
        builder.setNegativeButton("NO") { dialogInterface, i ->
            // 이전 화면으로 돌아가기
            intent.putExtra("success/fail", "fail")
            setResult(RESULT_OK, intent);
            finish()
        }
        builder.show()
    }



}