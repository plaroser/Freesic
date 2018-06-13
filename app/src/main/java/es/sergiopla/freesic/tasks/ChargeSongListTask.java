package es.sergiopla.freesic.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
    final StringBuilder builder = new StringBuilder();
    Context context;
    HttpURLConnection connection = null;
    BufferedReader reader = null;

    private AdapterSong songArrayAdapter;
    private ListView listView;
    private List<Song> songList;
    private String tittle;
    private TextView textViewTittle;

    public ChargeSongListTask(Context context, ListView listView, TextView textViewTittle, List<Song> songList) {
        super();
        this.context = context;
        this.listView = listView;
        this.textViewTittle = textViewTittle;
        this.songList = songList;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        songList = new ArrayList<>();
    }

    protected String doInBackground(String... params) {
        try {
            URL url = new URL(MainActivity.STRING_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            JSONObject jsonObject = new JSONObject(buffer.toString()).getJSONObject("feed");
            tittle = jsonObject.getString("title");
            JSONArray songs = jsonObject.getJSONArray("results");
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

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        songArrayAdapter = new AdapterSong(context, R.layout.song_item, songList);
        listView.setAdapter(songArrayAdapter);
        textViewTittle.setText(tittle);
        Toast.makeText(context, "Terminado tamaño: " + songList.size(), Toast.LENGTH_SHORT).show();
    }


}
