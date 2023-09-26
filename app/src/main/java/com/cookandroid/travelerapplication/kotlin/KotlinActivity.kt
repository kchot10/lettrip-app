package com.cookandroid.travelerapplication.kotlin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.travelerapplication.R
import com.cookandroid.travelerapplication.task.InsertData_Place
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KotlinActivity : AppCompatActivity() {
    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK 43a9d1617d8fb89af04db23790b3dd22"  // REST API 키
    }

    private val listItems = arrayListOf<ListLayout>()   // 리사이클러 뷰 아이템
    private val listAdapter = ListAdapter(listItems)    // 리사이클러 뷰 어댑터
    private var pageNumber = 1      // 검색 페이지 번호
    private var keyword = ""        // 검색 키워드
    private var place_address_and_placeName = ""

    private lateinit var mapView: MapView
    private lateinit var rvList: RecyclerView
    private lateinit var etSearchField: EditText
    private lateinit var tvPageNumber: TextView
    private lateinit var btnSearch: Button
    private lateinit var btnPrevPage: Button
    private lateinit var btnNextPage: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)

        mapView = findViewById(R.id.mapView)
        rvList = findViewById(R.id.rv_list)
        btnSearch = findViewById(R.id.btn_search)
        btnPrevPage = findViewById(R.id.btn_prevPage)
        btnNextPage = findViewById(R.id.btn_nextPage)
        etSearchField = findViewById(R.id.et_search_field)
        tvPageNumber = findViewById(R.id.tv_pageNumber)

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

                Log.d("listItems", "listItems[position].category_group_code: "+listItems[position].category_group_code)
                Log.d("listItems", "listItems[position].category_group_name: "+listItems[position].category_group_name)
                mapView.setMapCenterPointAndZoomLevel(mapPoint, 1, true)
            }
        })

        // 검색 버튼
        btnSearch.setOnClickListener {
            keyword = etSearchField.text.toString()
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

        try {
            if (!intent.getStringExtra("place_address_and_placeName").isNullOrEmpty()){
                place_address_and_placeName = intent.getStringExtra("place_address_and_placeName").toString();
                etSearchField.setText(place_address_and_placeName);
                btnSearch.performClick();
            }
        }catch(e: Exception){
            Log.e("Error", "place_address_and_placeName is empty 2");
        }
    }

    // 키워드 검색 함수
    private fun searchKeyword(keyword: String, page: Int) {
        val retrofit = Retrofit.Builder()          // Retrofit 구성
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(KakaoAPI::class.java)            // 통신 인터페이스를 객체로 생성
        val call = api.getSearchKeyword(API_KEY, keyword, page)    // 검색 조건 입력

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
                // 결과를 리사이클러 뷰에 추가
                val item = ListLayout(document.place_name,
                    document.road_address_name,
                    document.address_name,
                    document.x.toDouble(),
                    document.y.toDouble(),
                    document.category_group_code,
                    document.category_group_name)
                listItems.add(item)

                // 지도에 마커 추가
                val point = MapPOIItem()
                point.apply {
                    itemName = document.place_name
                    mapPoint = MapPoint.mapPointWithGeoCoord(document.y.toDouble(),
                        document.x.toDouble())
                    markerType = MapPOIItem.MarkerType.BluePin
                    selectedMarkerType = MapPOIItem.MarkerType.RedPin
                }
                mapView.addPOIItem(point)
            }
            listAdapter.notifyDataSetChanged()

            btnNextPage.isEnabled = !searchResult.meta.is_end // 페이지가 더 있을 경우 다음 버튼 활성화
            btnPrevPage.isEnabled = pageNumber != 1             // 1페이지가 아닐 경우 이전 버튼 활성화

        } else {
            // 검색 결과 없음
            Toast.makeText(this, "검색 결과가 없습니다", Toast.LENGTH_SHORT).show()
        }
    }

}