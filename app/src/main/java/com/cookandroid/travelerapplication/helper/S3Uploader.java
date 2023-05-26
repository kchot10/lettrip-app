package com.cookandroid.travelerapplication.helper;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transfermanager.Upload;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class S3Uploader {
    private static final String ACCESS_KEY = ""; // AWS 액세스 키
    private static final String SECRET_KEY = ""; // AWS 비밀 액세스 키
    private static final String BUCKET_NAME = ""; // 업로드할 버킷 이름
    private static final String REGION = Regions.AP_NORTHEAST_2.getName(); // 업로드할 리전

    private TransferUtility transferUtility;

    private String mStoredFileName;

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

    public void uploadImage(Uri imageUri, final OnUploadListener listener) {
        String filePath = getRealPathFromUri(mContext, imageUri);
        File file = new File(filePath);
        mStoredFileName = getStoredFileName(file.getName());
        TransferObserver observer = transferUtility.upload(
                BUCKET_NAME, // 버킷 이름
                mStoredFileName, // 업로드할 파일 이름 (원하는 대로 수정 가능)
                file, // 업로드할 파일 객체
                CannedAccessControlList.PublicRead
        );

        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state == TransferState.COMPLETED) {
                    String imageUrl = "https://" + BUCKET_NAME + ".s3." + REGION + ".amazonaws.com/" + mStoredFileName; // 업로드된 파일 URL
                    String fileSize = String.valueOf(file.length());
                    String originalFileName = file.getName();
                    String storedFileName = mStoredFileName;

                    listener.onSuccess(imageUrl, fileSize, originalFileName, storedFileName);
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

    private String getStoredFileName(String fileName) {
        // UUID를 사용하여 저장된 파일 이름 생성
        String uuid = UUID.randomUUID().toString();
        int extensionIndex = fileName.lastIndexOf(".");
        if (extensionIndex != -1) {
            String extension = fileName.substring(extensionIndex);
            return uuid + extension;
        }
        return uuid;
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
        void onSuccess(String imageUrl,String fileSize,String originalFileName,String storedFileName);
        void onFailure();
    }
}