# Lettrip (레트립)

**여행 기록을 공유하고, 함께 여행할 친구를 찾고, 현지에서 미션에 도전하는 Android 앱**
대학교 졸업작품 (5명 팀) / 2023년 3월 ~ 11월

<p align="center">
  <img src="docs/images/lettrip_review.png" width="30%" />
  <img src="docs/images/lettrip_chat.png" width="30%" />
  <img src="docs/images/lettrip_mission.png" width="30%" />
</p>

| 여행 기록·후기 공유 | 동행자 매칭·채팅 | 위치 기반 현지 미션 |
|:---:|:---:|:---:|
| 다녀온 장소를 사진과 함께 기록·공유 | 함께 여행할 친구를 찾아 대화 | QR코드·GPS로 도착 인증 |

---

## 담당

**Android 클라이언트** (화면 구현, PHP API 연동)
팀 전체 커밋 **426건 중 268건** 담당

## 주요 기능

| 기능 | 내용 |
|---|---|
| 회원 | 회원가입·로그인, 이메일 인증, 소셜 로그인 (Google / Kakao / Naver) |
| 게시판 | 글·댓글·답글 작성, 정렬 (최신순·조회수순) |
| 여행 기록 | 여행·코스·장소 등록, 사진 후기 (AWS S3 업로드) |
| 검색·좋아요 | 지역·테마 검색, 좋아요한 여행 목록 |
| 여행 미션 | QR코드·GPS 도착 인증, 제한 시간, 포인트, 랭킹 |

## 사용 기술

`Java` `Kotlin(일부)` `Android Studio` `PHP` `MySQL` `MongoDB` `AWS S3` `Kakao Map API` `GitHub`

## 시스템 구성

```
Android 앱 (담당) ⇄ PHP API (중간 서버) ⇄ MySQL (회원·게시글·여행·후기·미션)
                                          + AWS S3 (이미지) / MongoDB (채팅)
```

## 고민한 점

- **앱에서 DB에 직접 연결하지 않는 구조로 변경**: DB 접속 정보를 앱에 넣으면 앱을 분석당했을 때 유출될 위험이 있어서, 서버 담당과 상의해 PHP를 중간 서버로 두는 구조로 바꿨습니다.
- **데이터 저장소 변경 (Firebase → MySQL)**: 회원·여행·후기 등 데이터 사이의 관계가 많아지면서, 관계형 데이터를 다루기 쉬운 MySQL로 옮겼습니다.
