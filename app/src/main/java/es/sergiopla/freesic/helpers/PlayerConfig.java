package es.sergiopla.freesic.helpers;

import android.util.Log;

import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

import es.sergiopla.freesic.views.MainActivity;

public class PlayerConfig {
    private boolean isPlaying;
    public static final String API_KEY = "AIzaSyDz1pqkPjClj58_X05H1zgNSyahW3CICE8";

    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;
    private static String videoId;

    public PlayerConfig(YouTubePlayerView youTubePlayerView) {
        this.youTubePlayerView = youTubePlayerView;
        this.isPlaying = false;
    }

    public void loadVideo(String videoId) {
        PlayerConfig.videoId = videoId;
        youTubePlayer = null;
        youTubePlayerView.initialize(new YouTubePlayerInitListener() {
            @Override
            public void onInitSuccess(final YouTubePlayer initializedYouTubePlayer) {
                initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        youTubePlayer = initializedYouTubePlayer;
                        Log.v(MainActivity.LOG_ID, "Cargar video: " + PlayerConfig.videoId);
                        youTubePlayer.loadVideo(PlayerConfig.videoId, 0);
                        isPlaying = true;
                    }
                });
            }
        }, true);
    }

    public void pauseVideo() {
        youTubePlayer.pause();
        isPlaying = false;
    }

    public void playVideo() {
        if (youTubePlayer != null)
            youTubePlayer.play();
        isPlaying = true;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}

