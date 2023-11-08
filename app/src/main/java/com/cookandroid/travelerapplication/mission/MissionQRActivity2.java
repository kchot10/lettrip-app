package com.cookandroid.travelerapplication.mission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cookandroid.travelerapplication.R;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

public class MissionQRActivity2 extends AppCompatActivity {
        final int CAMERA_PERMISSION_REQUEST = 100;
        private LocationManager locationManager;
        private LocationListener locationListener;

        DecoratedBarcodeView barcodeView;
        boolean torchOn = false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_mission_qr_scanner);

            barcodeView = findViewById(R.id.barcode_scanner);

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
            } else {
                startCamera();
            }
        }

        @Override
        protected void onResume() {
            super.onResume();
            barcodeView.resume();
        }

        @Override
        protected void onPause() {
            super.onPause();
            barcodeView.pause();
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == CAMERA_PERMISSION_REQUEST) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                } else {
                    Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }

        private void startCamera() {
            barcodeView.decodeContinuous(new BarcodeCallback() {
                @Override
                public void barcodeResult(BarcodeResult result) {
                    // QR 코드 스캔 결과 처리
                    String barcodeValue = result.getText();
                    //Toast.makeText(getApplicationContext(), "Scanned: " + barcodeValue, Toast.LENGTH_SHORT).show();

                    //스캔한 위치 정보와 현재 사용자의 위치 정보 비교 후 포인트 지급
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            double latitude = location.getLatitude(); //위도
                            double longitude = location.getLongitude(); //경도

                            String locationString = "GEO:" + latitude + "," + longitude;

                            if(locationString.equals(barcodeValue)){
                                Toast.makeText(getApplicationContext(), "미션 완료! 700 포인트가 지급되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MissionQRActivity.class);
                                startActivity(intent);
                            } else{
                                Toast.makeText(getApplicationContext(), "미션 실패! 다시 한 번 QR 코드를 인식해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                }

                @Override
                public void possibleResultPoints(List<ResultPoint> resultPoints) {
                    // 스캔 가능한 QR 코드의 위치 표시 등 추가 동작을 수행할 수 있음
                }
            });
        }
}
