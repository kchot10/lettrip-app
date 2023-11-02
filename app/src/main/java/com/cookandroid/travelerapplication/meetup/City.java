package com.cookandroid.travelerapplication.meetup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

    public class City {
        static final List<String> SEOUL = Arrays.asList(
                "서울"
        );
        static final List<String> BUSAN = Arrays.asList(
                "부산"
        );
        static final List<String> DAEGU = Arrays.asList(
                "대구"
        );
        static final List<String> INCHEON = Arrays.asList(
                "인천"
        );
        static final List<String> GWANGJU = Arrays.asList(
                "광주"
        );
        static final List<String> DAEJEON = Arrays.asList(
                "대전"
        );
        static final List<String> ULSAN = Arrays.asList(
                "울산"
        );
        static final List<String> SEJONG = Arrays.asList(
                "세종"
        );
        public static final List<String> GYEONGGI_CITY = Arrays.asList(
                "수원시", "고양시", "용인시", "성남시", "부천시",
                "화성시", "안산시", "남양주시", "안양시", "평택시",
                "시흥시", "파주시", "의정부시", "김포시", "광주시",
                "광명시", "군포시", "하남시", "오산시", "양주시",
                "이천시", "구리시", "안성시", "포천시", "의왕시",
                "양평군", "여주시", "동두천시", "가평군", "과천시",
                "연천군"
        );

        public static final List<String> GANGWON_CITY = Arrays.asList(
                "삼척시", "화천군", "고성군", "철원군", "횡성군",
                "춘천시", "강릉시", "양양군", "원주시", "정선군",
                "속초시", "태백시", "인제군", "영월군", "양구군",
                "홍천군", "평창군", "동해시"
        );

        static final List<String> CHUNGCHEONG_BUKDO_CITY = Arrays.asList(
                "청주시", "충주시", "제천시", "보은군", "옥천군",
                "영동군", "증평군", "진천군", "괴산군", "음성군",
                "단양군"
        );

        static final List<String> CHUNGCHEONG_NAMDO_CITY = Arrays.asList(
                "천안시", "공주시", "보령시", "아산시", "서산시",
                "논산시", "계룡시", "당진시", "금산군", "부여군",
                "서천군", "청양군", "홍성군", "예산군", "태안군"
        );

        static final List<String> JEOLLA_NAMDO_CITY = Arrays.asList(
                "목포시", "여수시", "순천시", "나주시", "광양시",
                "담양군", "곡성군", "구례군", "고흥군", "보성군",
                "화순군", "장흥군", "강진군", "해남군", "영암군",
                "무안군", "함평군", "영광군", "장성군", "완도군",
                "진도군", "신안군"
        );

        static final List<String> JEOLLA_BUKDO_CITY = Arrays.asList(
                "전주시", "군산시", "익산시", "정읍시", "남원시",
                "김제시", "완주군", "진안군", "무주군", "장수군",
                "임실군", "순창군", "고창군", "부안군"
        );

        static final List<String> GYEONGSANG_NAMDO_CITY = Arrays.asList(
                "창원시", "진주시", "통영시", "사천시", "김해시",
                "밀양시", "거제시", "양산시", "의령군", "함안군",
                "창녕군", "고성군", "남해군", "하동군", "산청군",
                "함양군", "거창군", "합천군"
        );

        static final List<String> GYEONGSANG_BUKDO_CITY = Arrays.asList(
                "포항시", "경주시", "김천시", "안동시", "구미시",
                "영주시", "영천시", "상주시", "문경시", "경산시",
                "의성군", "청송군", "영양군", "영덕군", "청도군",
                "고령군", "성주군", "칠곡군", "예천군", "봉화군",
                "울진군", "울릉군"
        );
        static final List<String> JEJU_CITY = Arrays.asList(
                "제주시","서귀포시"
        );
        public static List<String> getCityNames() {
            List<String> cityNames = new ArrayList<>();
            cityNames.addAll(GYEONGGI_CITY);
            cityNames.addAll(GANGWON_CITY);
            cityNames.addAll(CHUNGCHEONG_BUKDO_CITY);
            cityNames.addAll(CHUNGCHEONG_NAMDO_CITY);
            cityNames.addAll(JEOLLA_NAMDO_CITY);
            cityNames.addAll(JEOLLA_BUKDO_CITY);
            cityNames.addAll(GYEONGSANG_NAMDO_CITY);
            cityNames.addAll(GYEONGSANG_BUKDO_CITY);
            cityNames.addAll(JEJU_CITY);


            return cityNames;
        }

    }


