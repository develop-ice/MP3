package com.android.mp3;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity {
    // UI
    private Button btnPlayPause, btnNext, btnPrevious;
    private SeekBar volumeSeekBar, musicSeekBar;
    private TextView tvPregress, tvTotalTime, tvFileName;
    private String filePath;
    private int position;
    private ArrayList<String> list;
    // MediaPlayer
    private MediaPlayer mediaPlayer;
    // Handler
    private Runnable runnable;
    private Handler handler;
    private int totalTime;

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

        setupSeekBarVolume();

        setupSeekBarProgress();

    }

    private String createTimeLabel(int currentPosition) {
        // 1 min = 60 sec
        // 1 sec = 1000 milliseconds

        String timeLabel;
        int minute, second;

        minute = currentPosition / 1000 / 60;
        second = currentPosition / 1000 % 60;

        if (second < 10) {
            timeLabel = minute + ":0" + second;
        } else {
            timeLabel = minute + ":" + second;
        }
        return timeLabel;
    }

    private void setupSeekBarProgress() {
        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    musicSeekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // Handler
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                int currentPosition = mediaPlayer.getCurrentPosition();
                musicSeekBar.setMax(totalTime);
                musicSeekBar.setProgress(currentPosition);
                totalTime = mediaPlayer.getDuration();
                handler.postDelayed(runnable, 1000);

                // Label
                String elapsedTime = createTimeLabel(currentPosition);
                String lastTime = createTimeLabel(totalTime);

                tvPregress.setText(elapsedTime);
                tvTotalTime.setText(lastTime);

                if (elapsedTime.equals(lastTime)) {
                    mediaPlayer.reset();
                    if (position == list.size() - 1) {
                        position = 0;
                    } else {
                        position++;
                    }
                    play();
                }
            }
        };
        handler.post(runnable);
    }

    private void setupSeekBarVolume() {
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    volumeSeekBar.setProgress(progress);
                    float volumeLevel = progress / 100f;
                    mediaPlayer.setVolume(volumeLevel, volumeLevel);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void onClickPrevious() {
        setupSeekBarProgress();
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
        String title = getIntent().getStringExtra("TITLE");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
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
        ImageView imgAlbum = findViewById(R.id.img_album);

        // GLIDE
        Glide.with(this)
                .load(R.drawable.speakers)
                .asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgAlbum);

    }
}
