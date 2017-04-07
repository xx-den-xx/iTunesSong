package ru.bda.itunessong.view;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.*;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.bda.itunessong.R;
import ru.bda.itunessong.model.data.Result;

public class SongActivity extends AppCompatActivity implements View.OnClickListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.image)
    ImageView image;

    @Bind(R.id.name_track)
    TextView nameTrack;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Bind(R.id.btn_play)
    ImageButton btnPlay;

    @Bind(R.id.btn_pause)
    ImageButton btnPause;

    private Result song;
    private MediaPlayer player;
    private Handler songHandler;

    private Runnable playerRunnable = new Runnable() {

        @Override
        public void run() {
            if (player != null && player.isPlaying())
                progressBar.setProgress(player.getCurrentPosition());
            songHandler.postDelayed(this, 10);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseMP();
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            song = (Result) extras.getSerializable("song");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image.setTransitionName(getString(R.string.transition_image_track));
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.explode));
        }

        int widthScreen =  ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        Picasso.with(this)
                .load(song.getArtworkUrl100())
                .resize(widthScreen, widthScreen)
                .centerCrop()
                .noFade()
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                        supportStartPostponedEnterTransition();
                    }
                });

        songHandler = new Handler();
        toolbar.setTitle(song.getArtistName());
        nameTrack.setText(song.getTrackName());
        btnPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_play:
                try {
                    if (player == null) {
                        player = new MediaPlayer();
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDataSource(song.getPreviewUrl());
                        player.setLooping(false);
                        player.setOnPreparedListener(this);
                        player.setOnCompletionListener(this);
                        player.prepareAsync();
                        progressBar.setProgress(0);
                    } else if (player != null && !player.isPlaying()){
                        player.start();
                    }
                } catch (IOException e) {
                    // TODO: handle exception
                }
                break;
            case R.id.btn_pause:
                if (player != null && player.isPlaying()) {
                    player.pause();
                }
                break;
        }
    }

    private void releaseMP() {
        if (player != null) {
            try {
                progressBar.setProgress(0);
                player.release();
                player = null;
                songHandler.removeCallbacks(playerRunnable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPrepared(final MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        progressBar.setMax(player.getDuration());
        songHandler.postDelayed(playerRunnable, 10);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMP();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        releaseMP();
    }
}
