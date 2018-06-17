package es.sergiopla.freesic.views;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

import java.util.HashMap;
import java.util.List;

import es.sergiopla.freesic.R;
import es.sergiopla.freesic.adapters.tabs.Tabsadapter;
import es.sergiopla.freesic.fragment.ViewListFragment;
import es.sergiopla.freesic.helpers.PlayerConfig;
import es.sergiopla.freesic.models.Song;
import es.sergiopla.freesic.tasks.SearchYouTube;

public class MainActivity extends AppCompatActivity implements TabListener {
    public static final String LOG_ID = "Freesic_Sergio";
    public static final String[] STRING_URL = {"https://rss.itunes.apple.com/api/v1/es/itunes-music/", "/all/100/non-explicit.json"};
    public static final String CANCIONES_DEL_MOMENTO = "Canciones del momento";
    public static final String MUSICA_NUEVA = "Música nueva";
    public static final String LANZAMIENTOS_RECIENTES = "Lanzamientos recientes";
    public static final String TOP_CANCIONES = "Top canciones";
    public static final String KEY_LIST = "list";
    public static final String KEY_TITTLE = "tittle";
    public static int listSize, currentSong;
    public static List<Song> songList;
    public static String URL;

    private ViewPager tabsviewPager;
    private ActionBar mActionBar;
    private Tabsadapter mTabsAdapter;
    private YouTubePlayerView youTubePlayerView;
    private PlayerConfig playerConfig;
    private ImageButton imageButtonNext, imageButtonPlayPause, imageButtonPrevious;
    private ListView listViewSongs;
    private SearchYouTube searchYouTube;
    private AdapterView.OnItemClickListener onItemClickListenerList;
    private MainActivity mainActivity;
    private TextView textViewTitleSong;
    private ProgressBar progressBarPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        setContentView(R.layout.activity_main);
        tabsviewPager = findViewById(R.id.tabspager);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        imageButtonPrevious = findViewById(R.id.imageButtonPrevious);
        imageButtonPlayPause = findViewById(R.id.imageButtonPlayPause);
        imageButtonNext = findViewById(R.id.imageButtonNext);
        textViewTitleSong = findViewById(R.id.textViewTitleSong);
        textViewTitleSong.setSelected(true);
        mTabsAdapter = new Tabsadapter(getSupportFragmentManager());
        tabsviewPager.setAdapter(mTabsAdapter);
        progressBarPlay = findViewById(R.id.progressBarPlay);
        onPlaying(false);
        progressBarPlay.setVisibility(View.INVISIBLE);

        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getSupportActionBar().setDisplayShowHomeEnabled(false);     // hides action bar icon
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // hides action bar title
        Tab friendstab = getSupportActionBar().newTab().setText(R.string.top_lists).setTabListener(this);
        Tab communitytab = getSupportActionBar().newTab().setText(R.string.actual_list).setTabListener(this);
        Tab publicprofiletab = getSupportActionBar().newTab().setText(R.string.search).setTabListener(this);

        getSupportActionBar().addTab(friendstab);
        getSupportActionBar().addTab(communitytab);
        getSupportActionBar().addTab(publicprofiletab);

        imageButtonPlayPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playPauseVideo();
            }
        });
        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextVideo();
            }
        });
        imageButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevVideo();
            }
        });

        tabsviewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                getSupportActionBar().setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        onItemClickListenerList = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = (Song) listViewSongs.getItemAtPosition(position);
                String itemTitle = song.getTitle();
                searchVideo(itemTitle, position);
            }
        };

        textViewTitleSong.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
//                Set ic_pause
                imageButtonPlayPause.setImageResource(R.drawable.ic_pause);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
    }

    public static HashMap<String, String> getListItunes() {
        HashMap<String, String> musicHashMap = new HashMap<>();
        musicHashMap.put(CANCIONES_DEL_MOMENTO, STRING_URL[0] + "hot-tracks" + STRING_URL[1]);
        musicHashMap.put(MUSICA_NUEVA, STRING_URL[0] + "new-music" + STRING_URL[1]);
        musicHashMap.put(LANZAMIENTOS_RECIENTES, STRING_URL[0] + "recent-releases" + STRING_URL[1]);
        musicHashMap.put(TOP_CANCIONES, STRING_URL[0] + "top-songs" + STRING_URL[1]);
        return musicHashMap;
    }

    /**
     * Change active tab
     *
     * @param index index of selected tab
     */
    public void changeTab(int index) {
        if (index == Tabsadapter.INDEX_FRIENDS_FRAGMENT) {
            ViewListFragment fragment = (ViewListFragment) mTabsAdapter.getItem(index);
            fragment.loadList();
        }
        getSupportActionBar().setSelectedNavigationItem(index);
    }

    /**
     * set play or pause this playing
     */
    public void playPauseVideo() {
        if (playerConfig != null) {
            if (playerConfig.isPlaying()) {
                playerConfig.pauseVideo();
                imageButtonPlayPause.setImageResource(R.drawable.ic_play_arrow);
            } else {
                playerConfig.playVideo();
                imageButtonPlayPause.setImageResource(R.drawable.ic_pause);
            }
        }
    }

    /**
     * Play the next video if is possible
     */
    public void nextVideo() {
        int nextPosition = currentSong + 1;
        if (nextPosition < listSize && nextPosition >= 0) {
            Log.v(LOG_ID, "Siquiente");
            Song nextSong = songList.get(nextPosition);
            String nextTitle = nextSong.getTitle();
            Log.v(LOG_ID, "next title: " + nextTitle);
            searchVideo(nextTitle, nextPosition);
        }
    }

    /**
     * Play the prev video if is possible
     */
    public void prevVideo() {
        int prevPosition = currentSong - 1;
        if (prevPosition < listSize && prevPosition >= 0) {
            Log.v(LOG_ID, "Anterior");
            Song prevSong = songList.get(prevPosition);
            String prevTitle = prevSong.getTitle();
            Log.v(LOG_ID, "prev title: " + prevTitle);
            searchVideo(prevTitle, prevPosition);
        } else {
            Log.v(LOG_ID, "No ha sido posible retroceder a la posicion: " + prevPosition + " size: " + listSize);

        }
    }

    /**
     * Search a video from a title
     *
     * @param title    Title to search
     * @param position Position of title
     */
    public void searchVideo(String title, int position) {
        Log.v(LOG_ID, "Reproduciendo: " + title);

        listSize = songList.size();
        currentSong = position;
        searchYouTube = new SearchYouTube(mainActivity, title, mainActivity);
        searchYouTube.execute();
    }

    /**
     * Play a video from a id
     *
     * @param idVideo Id for play
     */
    public void playVideo(String idVideo) {
        playerConfig = new PlayerConfig(youTubePlayerView);
        playerConfig.loadVideo(idVideo);
    }

    public void setListViewSongs(ListView listViewSongs) {
        this.listViewSongs = listViewSongs;
        this.listViewSongs.setOnItemClickListener(onItemClickListenerList);
    }

    public void setSongList(List<Song> songList) {
        MainActivity.songList = songList;
    }

    @Override
    public void onTabReselected(Tab arg0, FragmentTransaction arg1) {

    }

    @Override
    public void onTabSelected(Tab selectedtab, FragmentTransaction arg1) {
        int position = selectedtab.getPosition();
        tabsviewPager.setCurrentItem(position); //update tab position on tap

    }

    @Override
    public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {

    }

    public void cargarLista(String url) {
        MainActivity.URL = url;
    }

    /**
     * It establishes that the reproduction is active
     *
     * @param isPlaying
     */
    public void onPlaying(boolean isPlaying) {
        imageButtonPrevious.setEnabled(isPlaying);
        imageButtonPlayPause.setEnabled(isPlaying);
        imageButtonNext.setEnabled(isPlaying);
        progressBarPlay.setVisibility((isPlaying) ? View.INVISIBLE : View.VISIBLE);
    }

    public void setTittleSong(String tittleSong) {
        if (tittleSong != null) {
            textViewTitleSong.setText(tittleSong);
        } else {
            textViewTitleSong.setText(R.string.cancion_no_elegida);
        }
    }
}