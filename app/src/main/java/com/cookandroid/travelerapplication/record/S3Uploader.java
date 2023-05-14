package com.cookandroid.travelerapplication.record;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class S3Uploader {
    private static final String ACCESS_KEY = ""; // AWS 액세스 키
    private static final String SECRET_KEY = ""; // AWS 비밀 액세스 키
    private static final String BUCKET_NAME = ""; // 업로드할 버킷 이름
    private static final String REGION = Regions.AP_NORTHEAST_2.getName(); // 업로드할 리전

    private TransferUtility transferUtility;

    Context mContext;

    public S3Uploader(Context context) {
        mContext = context;
        BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3Client s3Client = new AmazonS3Client(credentials);
        transferUtility = TransferUtility.builder()
                .context(context.getApplicationContext())
                .defaultBucket(BUCKET_NAME)
                .s3Client(s3Client)
                .build();
    }
    public void displayImageFromUri(Uri imageUri, ImageView imageView) {
        try {
            InputStream inputStream = mContext.getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void uploadImage(Uri imageUri, final OnUploadListener listener) {
        String filePath = getRealPathFromUri(mContext, imageUri);
        File file = new File(filePath);
        TransferObserver observer = transferUtility.upload(
                BUCKET_NAME, // 버킷 이름
                file.getName(), // 업로드할 파일 이름 (원하는 대로 수정 가능)
                file // 업로드할 파일 객체
        );
        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state == TransferState.COMPLETED) {
                    String imageUrl = "https://" + BUCKET_NAME + ".s3." + REGION + ".amazonaws.com/" + file.getName(); // 업로드된 파일 URL
                    listener.onSuccess(imageUrl);
                } else if (state == TransferState.FAILED || state == TransferState.CANCELED) {
                    listener.onFailure();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int progress = (int) (bytesCurrent * 100 / bytesTotal);
                listener.onProgress(progress);
            }

            @Override
            public void onError(int id, Exception ex) {
                listener.onFailure();
            }
        });
    }

    public String getRealPathFromUri(Context context, Uri uri) {
        String filePath = "";
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(column_index);
            }
            cursor.close();
        }
        return filePath;
    }


    public interface OnUploadListener {
        void onProgress(int progress);
        void onSuccess(String imageUrl);
        void onFailure();
    }
}
