package es.sergiopla.freesic.fragment;

/**
 * Created by Sergio on 5/24/2018.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import es.sergiopla.freesic.R;
import es.sergiopla.freesic.models.Song;
import es.sergiopla.freesic.tasks.ChargeSongListTask;
import es.sergiopla.freesic.tasks.SearchYouTube;
import es.sergiopla.freesic.views.MainActivity;


public class Friendsfragment extends Fragment {
    private Button buttonGetWeb;
    private TextView textViewTitulo;
    private ListView listViewSongs;
    private Context context;
    private SearchYouTube searchYouTube;
    private List<Song> songList;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friendsview, container, false);
        //  ___       _ _   _       _ _          _   _
        // |_ _|_ __ (_) |_(_) __ _| (_)______ _| |_(_) ___  _ __
        //  | || '_ \| | __| |/ _` | | |_  / _` | __| |/ _ \| '_ \
        //  | || | | | | |_| | (_| | | |/ / (_| | |_| | (_) | | | |
        // |___|_| |_|_|\__|_|\__,_|_|_/___\__,_|\__|_|\___/|_| |_|
        super.onCreate(savedInstanceState);
        context = getActivity();
        buttonGetWeb = view.findViewById(R.id.buttonGetWeb);
        textViewTitulo = view.findViewById(R.id.textViewTittle);
        listViewSongs = view.findViewById(R.id.listViewSongs);
        mainActivity = ((MainActivity) getActivity());
//        cargarLista();

        buttonGetWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarLista();
            }
        });

//        listViewSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Song song = (Song) listViewSongs.getItemAtPosition(position);
//                String itemTitle = song.getTitle();
//                MainActivity.listSize = listViewSongs.getLeft();
//                MainActivity.currentSong = position;
//                searchYouTube = new SearchYouTube(context, itemTitle, ((MainActivity) getActivity()));
//                searchYouTube.execute();
//            }
//        });
        return view;
    }

    private void cargarLista() {
        ChargeSongListTask chargeSongListTask = new ChargeSongListTask(context, listViewSongs, textViewTitulo, songList);
        chargeSongListTask.execute();
        mainActivity.setListViewSongs(listViewSongs);
        mainActivity.setSongList(songList);
    }

    public ListView getListViewSongs() {
        return this.listViewSongs;
    }

    public List<Song> getListSongs() {
        return songList;
    }

    private void searchYoutube(String title) {
        searchYouTube = new SearchYouTube(context, title, ((MainActivity) getActivity()));
        searchYouTube.execute();
    }
}