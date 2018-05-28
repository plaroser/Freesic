package es.sergiopla.freesic.helpers;

import android.content.Context;

import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

public class PlayerConfig {
    public static final String API_KEY = "AIzaSyDz1pqkPjClj58_X05H1zgNSyahW3CICE8";

    YouTubePlayerView youTubePlayerView;
    Context context;

    public PlayerConfig(YouTubePlayerView youTubePlayerView, Context context) {
        this.youTubePlayerView = youTubePlayerView;
        this.context = context;
    }

    public void playVideo(String videoId) {
        final String idVideo = videoId;
        youTubePlayerView.initialize(new YouTubePlayerInitListener() {
            @Override
            public void onInitSuccess(final YouTubePlayer initializedYouTubePlayer) {
                initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        initializedYouTubePlayer.loadVideo(idVideo, 0);
                    }
                });
            }
        }, true);
    }
}

