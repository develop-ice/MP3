package com.android.mp3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // UI
    private RecyclerView recyclerView;
    // REQUEST CODE
    public static final int REQUEST_READ_EXTERNAL_STORAGE = 101;
    // Path
    public static final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath() + "/";
    private ArrayList<String> songList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        checkPermissions();

        setupAdapter();
    }

    private void setupAdapter() {
        MusicAdapter adapter = new MusicAdapter(songList, MainActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void checkPermissions() {
        // Check permissions
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            getAllAudioFiles();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getAllAudioFiles();
        }

    }

    private void getAllAudioFiles() {
        File mainFile = new File(MEDIA_PATH);
        File[] fileList = mainFile.listFiles();
        for (File file : fileList) {
            if (file.isDirectory()) {
                scanDirectory(file);
            } else {
                String path = file.getAbsolutePath();
                if (path.endsWith(".mp3")) {
                    songList.add(path);
                }
            }
        }
    }

    private void scanDirectory(File directory) {
        if (directory != null) {
            File[] fileList = directory.listFiles();
            for (File file : fileList) {
                if (file.isDirectory()) {
                    scanDirectory(file);
                } else {
                    String path = file.getAbsolutePath();
                    if (path.endsWith(".mp3")) {
                        songList.add(path);
                    }
                }
            }
        }
    }

    private void initView() {
        ImageView imgAlbum = findViewById(R.id.img_album);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_anim);
        imgAlbum.setAnimation(animation);
        imgAlbum.startAnimation(animation);
    }


}
