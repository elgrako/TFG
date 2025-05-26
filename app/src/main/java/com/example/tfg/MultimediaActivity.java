package com.example.tfg;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SeekBar;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class MultimediaActivity extends AppCompatActivity {

    private VideoView videoView;
    private SeekBar seekBar;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multimedia);

        videoView = findViewById(R.id.videoView);
        seekBar = findViewById(R.id.videoSeekBar);

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.abogaciavideo);
        videoView.setVideoURI(videoUri);
        videoView.start();

        videoView.setOnPreparedListener(mp -> {
            seekBar.setMax(videoView.getDuration());
            updateSeekBar();
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacksAndMessages(null);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateSeekBar();
            }
        });
    }

    private void updateSeekBar() {
        if (videoView.isPlaying()) {
            seekBar.setProgress(videoView.getCurrentPosition());
            handler.postDelayed(this::updateSeekBar, 200);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSeekBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        videoView.stopPlayback();
    }
}