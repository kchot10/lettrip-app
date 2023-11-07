package com.cookandroid.travelerapplication.kotlin

// REST API 통신 인터페이스
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoAPI3 {
    @GET("v2/local/geo/coord2regioncode.json")    // Keyword.json의 정보를 받아옴
    fun getSearchKeyword(
        @Header("Authorization") key: String,     // 카카오 API 인증키 [필수]
        @Query("x") x: String,  // x 좌표 파라미터 추가
        @Query("y") y: String,   // y 좌표  파라미터 추가
    ): Call<ResultSearchKeyword>    // 받아온 정보가 ResultSearchKeyword 클래스의 구조로 담김
}