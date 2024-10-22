package com.example.re_hw4;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.net.Uri;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 100;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonLoadImage = findViewById(R.id.buttonLoadImage);
        imageView = findViewById(R.id.imageView);
        buttonLoadImage.setOnClickListener(v -> {
                // 권한 체크
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_MEDIA_IMAGES)
                        != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE_PERMISSION);
                    } else {
                        loadImageFromDisk(); // 권한이 있으면 이미지 불러오기
                    }
        });
    }

    private void loadImageFromDisk() {
        // 이미지 파일의 절대 경로
        String imagePath = "/storage/emulated/0/Pictures/ANGEL.jpg";
        Uri contentUri = Uri.parse("file://" + imagePath);

        // 이미지를 ImageView에 설정
        imageView.setImageURI(contentUri);
        copyImageToInternalStorage(contentUri);

        // Toast 메시지로 이미지 경로 확인
        Toast.makeText(this, "이미지를 불러왔습니다: " + imagePath, Toast.LENGTH_SHORT).show();
    }

    private void copyImageToInternalStorage(Uri uri) {
        String fileName = "ANGEL.jpg"; // 복사할 이미지 파일 이름
        File directory = getFilesDir(); // 내부 저장소의 파일 디렉토리
        File file = new File(directory, fileName);

        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(file)) {
            if (inputStream != null) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                Toast.makeText(this, "이미지가 저장되었습니다: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "파일 복사 중 오류가 발생했습니다: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImageFromDisk(); // 권한이 허용된 경우 이미지 불러오기
            } else {
                Toast.makeText(this, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
