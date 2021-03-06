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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import es.sergiopla.freesic.R;
import es.sergiopla.freesic.models.Song;
import es.sergiopla.freesic.tasks.ChargeSongListTask;
import es.sergiopla.freesic.tasks.ChargeSongListTask.TypeQuery;
import es.sergiopla.freesic.tasks.SearchYouTube;
import es.sergiopla.freesic.views.MainActivity;


public class ViewListFragment extends Fragment {
    private TextView textViewTitulo;
    private ListView listViewSongs;
    private Context context;
    private SearchYouTube searchYouTube;
    private List<Song> songList;
    private MainActivity mainActivity;
    private ProgressBar progressBar;
    private TextView textViewNotList;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_lists_fragment_view, container, false);
        //  ___       _ _   _       _ _          _   _
        // |_ _|_ __ (_) |_(_) __ _| (_)______ _| |_(_) ___  _ __
        //  | || '_ \| | __| |/ _` | | |_  / _` | __| |/ _ \| '_ \
        //  | || | | | | |_| | (_| | | |/ / (_| | |_| | (_) | | | |
        // |___|_| |_|_|\__|_|\__,_|_|_/___\__,_|\__|_|\___/|_| |_|
        super.onCreate(savedInstanceState);
        context = getActivity();
        textViewTitulo = view.findViewById(R.id.textViewTittle);
        listViewSongs = view.findViewById(R.id.listViewSongs);
        mainActivity = ((MainActivity) getActivity());
        progressBar = view.findViewById(R.id.progressBar);
        textViewNotList = view.findViewById(R.id.textViewNotList);

        return view;
    }

    /**
     * Load a list from a URL established
     */
    public void loadList() {
        TypeQuery typeQuery = (MainActivity.URL.contains("itunes.apple.com/search")) ? TypeQuery.ITUNES : TypeQuery.RSS;
        ChargeSongListTask chargeSongListTask = new ChargeSongListTask(context, listViewSongs, textViewTitulo, songList, progressBar, typeQuery);
        chargeSongListTask.execute();
        textViewNotList.setVisibility(View.INVISIBLE);
        mainActivity.setListViewSongs(listViewSongs);
        mainActivity.setSongList(songList);
    }

    public ListView getListViewSongs() {
        return this.listViewSongs;
    }

    public List<Song> getListSongs() {
        return songList;
    }

    /**
     * Search in YouTube from a title song
     * @param title
     */
    private void searchYoutube(String title) {
        searchYouTube = new SearchYouTube(context, title, ((MainActivity) getActivity()));
        searchYouTube.execute();
    }
}