package es.sergiopla.freesic.helpers;

import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

public class PlayerConfig {
    private boolean isPlaying;
    public static final String API_KEY = "AIzaSyDz1pqkPjClj58_X05H1zgNSyahW3CICE8";

    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;

    public PlayerConfig(YouTubePlayerView youTubePlayerView) {
        this.youTubePlayerView = youTubePlayerView;
        this.isPlaying = false;
    }

    public void loadVideo(final String videoId) {
        youTubePlayerView.initialize(new YouTubePlayerInitListener() {
            @Override
            public void onInitSuccess(final YouTubePlayer initializedYouTubePlayer) {
                initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        youTubePlayer = initializedYouTubePlayer;
                        initializedYouTubePlayer.loadVideo(videoId, 0);
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
        youTubePlayer.play();
        isPlaying = true;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}

