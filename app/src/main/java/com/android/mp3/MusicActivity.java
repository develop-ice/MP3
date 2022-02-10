package com.android.mp3;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity {
    // UI
    private Button btnPlayPause, btnNext, btnPrevious;
    private SeekBar volumeSeekBar, musicSeekBar;
    private TextView tvPregress, tvTotalTime, tvFileName;
    // FILE
    private String title, filePath;
    private int position;
    private ArrayList<String> list;
    // MediaPlayer
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        initView();

        setupData();

        setupMediaPlayer();

        onCLickPlayPause();

        onClickPrevious();

        onCLickNext();

    }

    private void onClickPrevious() {
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.reset();
                if (position == 0) {
                    position = list.size() - 1;
                } else {
                    position--;
                }
                play();
            }
        });
    }

    private void onCLickPlayPause() {
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlayPause.setBackgroundResource(R.drawable.ic_play);
                } else {
                    mediaPlayer.start();
                    btnPlayPause.setBackgroundResource(R.drawable.ic_pause);
                }
            }
        });
    }

    private void onCLickNext() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.reset();
                if (position == list.size() - 1) {
                    position = 0;
                } else {
                    position++;
                }
                play();
            }
        });
    }

    private void setupMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupData() {
        title = getIntent().getStringExtra("TITLE");
        filePath = getIntent().getStringExtra("FILE_PATH");
        position = getIntent().getIntExtra("POSITION", 0);
        list = getIntent().getStringArrayListExtra("LIST");
        // set title
        tvFileName.setText(title);
    }

    private void play() {
        String newFilePath = list.get(position);
        try {
            mediaPlayer.setDataSource(newFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            btnPlayPause.setBackgroundResource(R.drawable.ic_pause);
            String newTitle = newFilePath.substring(newFilePath.lastIndexOf("/") + 1);
            tvFileName.setText(newTitle);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        btnPlayPause = findViewById(R.id.btn_play_pause);
        btnNext = findViewById(R.id.btn_next);
        btnPrevious = findViewById(R.id.btn_previous);
        volumeSeekBar = findViewById(R.id.volume_seek_bar);
        musicSeekBar = findViewById(R.id.music_seek_bar);
        tvPregress = findViewById(R.id.tv_progress);
        tvTotalTime = findViewById(R.id.tv_total_time);
        tvFileName = findViewById(R.id.tv_file_name_music);
    }
}
