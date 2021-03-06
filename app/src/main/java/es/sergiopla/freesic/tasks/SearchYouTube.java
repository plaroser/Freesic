package es.sergiopla.freesic.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.sergiopla.freesic.R;
import es.sergiopla.freesic.helpers.PlayerConfig;
import es.sergiopla.freesic.models.VideoItem;
import es.sergiopla.freesic.views.MainActivity;

/**
 * Created by Sergio on 5/28/2018.
 */

public class SearchYouTube extends AsyncTask<String, String, String> {
    private YouTube.Search.List query;
    private YouTube youtube;
    private Context context;
    private String keywords;
    VideoItem videoItem;
    MainActivity activity;

    public SearchYouTube(Context content, String keywords, MainActivity activity) {
        this.context = content;
        this.keywords = keywords;
        this.activity = activity;
        youtubeConnector(content);
    }

    /**
     * Connect youtube for a context
     *
     * @param context
     */
    private void youtubeConnector(Context context) {
        youtube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest hr) {
            }
        }).setApplicationName(this.context.getString(R.string.app_name)).build();

        try {
            query = youtube.search().list("id,snippet");
            query.setKey(PlayerConfig.API_KEY);
            query.setType("video");
            query.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)");
        } catch (IOException e) {
            Log.d(MainActivity.LOG_ID, "Conector YouTube fallida" + e);
        }
    }

    /**
     * Search a list of video from a keywords
     * @param keywords
     * @return
     */
    private List<VideoItem> search(String keywords) {
        query.setQ(keywords);
        Log.v(MainActivity.LOG_ID, "Buscando video: " + keywords);
        try {
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();

            List<VideoItem> items = new ArrayList();
            for (SearchResult result : results) {
                VideoItem item = new VideoItem();
                item.setTitle(result.getSnippet().getTitle());
                item.setDescription(result.getSnippet().getDescription());
                item.setThumbnailURL(result.getSnippet().getThumbnails().getDefault().getUrl());
                item.setId(result.getId().getVideoId());
                items.add(item);
            }
            return items;
        } catch (IOException e) {
            Log.d(MainActivity.LOG_ID, "Could not search: " + e);
            return null;
        }
    }

    /**
     * Search a video iten from a keywords
     * @param keywords
     * @return
     */
    private VideoItem viewVideo(String keywords) {
        List<VideoItem> videoItemList = search(keywords);
        if (videoItemList.size() > 0) {
            return videoItemList.get(0);
        } else {
            return null;
        }
    }

    protected void onPreExecute() {
        super.onPreExecute();
        activity.onPlaying(false);
    }

    @Override
    protected String doInBackground(String... strings) {
        videoItem = viewVideo(this.keywords);
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        String titleVideo = videoItem.getTitle();
        Log.v(MainActivity.LOG_ID, "Resultado busqueda: " + titleVideo + " | " + videoItem.getId());
        super.onPostExecute(result);
        activity.playVideo(videoItem.getId());
        activity.setTittleSong(this.keywords);
        activity.onPlaying(true);
    }
}