package com.alihaydar.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.alihaydar.musicplayer.databinding.ActivityMainBinding;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    MediaPlayer mediaPlayer;
    Handler handler=new Handler();
    double startTime=0;
    double finalTime=0;
    int forwardTime=10000;
    int backwardTime=10000;
    static int oneTimeOnly=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        mediaPlayer=MediaPlayer.create(this,R.raw.letitgo);
        binding.seekBar.setClickable(false);
        
        binding.buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();

            }
            
        });
        binding.buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });
        binding.buttonForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp=(int)startTime;
                if ((temp+forwardTime)<=finalTime){
                    startTime=startTime+forwardTime;
                    mediaPlayer.seekTo((int)startTime);
                }else {
                    Toast.makeText(MainActivity.this, "Can't jump forward", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.buttonBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp=(int)startTime;
                if ((temp-forwardTime)>0){
                    startTime=startTime-forwardTime;
                    mediaPlayer.seekTo((int)startTime);
                }else {
                    Toast.makeText(MainActivity.this, "Can't jump back", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
    }

    private void playMusic() {
        mediaPlayer.start();

        finalTime=mediaPlayer.getDuration();
        startTime=mediaPlayer.getCurrentPosition();

        if(oneTimeOnly==0){
            binding.seekBar.setMax((int) finalTime);
            oneTimeOnly=1;
        }
        binding.textView2.setText(String.format(
                "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long)finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long)finalTime)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)finalTime))
        ));
        binding.seekBar.setProgress((int)startTime);
        handler.postDelayed(UpdateSongTime,100);
    }
    private Runnable UpdateSongTime=new Runnable() {
        @Override
        public void run() {
            startTime= mediaPlayer.getCurrentPosition();
            binding.textView2.setText(String.format(
                    "%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long)finalTime),
                    TimeUnit.MILLISECONDS.toSeconds((long)finalTime)-
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)finalTime))
            ));
            binding.seekBar.setProgress((int)startTime);
            handler.postDelayed(this,100);

        }
    };
}