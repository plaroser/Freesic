package es.sergiopla.freesic.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import es.sergiopla.freesic.R;
import es.sergiopla.freesic.adapters.lists.AdapterSong;
import es.sergiopla.freesic.models.Song;
import es.sergiopla.freesic.views.MainActivity;

public class ChargeSongListTask extends AsyncTask<String, String, String> {
    public enum TypeQuery {RSS, ITUNES}

    final StringBuilder builder = new StringBuilder();
    Context context;
    HttpURLConnection connection = null;
    BufferedReader reader = null;

    private AdapterSong songArrayAdapter;
    private ListView listView;
    private List<Song> songList;
    private String tittle;
    private TextView textViewTittle;
    private ProgressBar progressBar;
    private TypeQuery typeQuery;

    public ChargeSongListTask(Context context, ListView listView, TextView textViewTittle, List<Song> songList, ProgressBar progressBar) {
        super();
        this.context = context;
        this.listView = listView;
        this.textViewTittle = textViewTittle;
        this.songList = songList;
        this.progressBar = progressBar;
        this.typeQuery = TypeQuery.RSS;
    }

    public ChargeSongListTask(Context context, ListView listView, TextView textViewTittle, List<Song> songList, ProgressBar progressBar, TypeQuery type) {
        this(context, listView, textViewTittle, songList, progressBar);
        this.typeQuery = type;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        songList = new ArrayList<>();
        Log.v(MainActivity.LOG_ID, "cargando lista ");
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);
        textViewTittle.setVisibility(View.INVISIBLE);
    }

    protected String doInBackground(String... params) {
        try {
            URL url = new URL(MainActivity.URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (typeQuery == TypeQuery.RSS) {
                JSONObject jsonObject = new JSONObject(buffer.toString()).getJSONObject("feed");
                getRSS(jsonObject);
            } else {
                searchITunes(new JSONObject(buffer.toString()));
            }
//            tittle = jsonObject.getString("title");
//            JSONArray songs = jsonObject.getJSONArray("results");
//            builder.append(songs.length());
//            if (songs.length() > 0) {
//                for (int i = 0; i < songs.length(); i++) {
//                    JSONObject jsonObjectSong = songs.getJSONObject(i);
//
//                    String tittle = jsonObjectSong.getString("name");
//                    String author = jsonObjectSong.getString("artistName");
//                    Song song = new Song(tittle, author);
//                    songList.add(song);
//                }
//            }
//                    builder.append(buffer.toString());


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void getRSS(JSONObject results) {
        try {
            tittle = results.getString("title");
            JSONArray songs = results.getJSONArray("results");
            builder.append(songs.length());
            if (songs.length() > 0) {
                for (int i = 0; i < songs.length(); i++) {
                    JSONObject jsonObjectSong = songs.getJSONObject(i);

                    String tittle = jsonObjectSong.getString("name");
                    String author = jsonObjectSong.getString("artistName");
                    Song song = new Song(tittle, author);
                    songList.add(song);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void searchITunes(JSONObject results) {
        try {
            Log.v(MainActivity.LOG_ID, "Buscando...");

            tittle = null;
            JSONArray songs = results.getJSONArray("results");
            builder.append(songs.length());
            if (songs.length() > 0) {
                for (int i = 0; i < songs.length(); i++) {
                    JSONObject jsonObjectSong = songs.getJSONObject(i);
                    String tittle = jsonObjectSong.getString("trackName");
                    Log.v(MainActivity.LOG_ID, tittle);
                    String author = jsonObjectSong.getString("artistName");
                    Song song = new Song(tittle, author);
                    songList.add(song);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        songArrayAdapter = new AdapterSong(context, R.layout.song_item, songList);
        listView.setAdapter(songArrayAdapter);
        if (tittle != null) {
            textViewTittle.setText(tittle);
        } else {
            textViewTittle.setText(R.string.resultado_busqueda);
        }
        songList = new ArrayList<>(songList);
        Log.v(MainActivity.LOG_ID, "List title| " + tittle + " |size| " + songList.size());
        MainActivity.songList = songList;
        progressBar.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
        textViewTittle.setVisibility(View.VISIBLE);
    }


}
