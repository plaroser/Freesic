package es.sergiopla.freesic.views;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

import java.util.List;

import es.sergiopla.freesic.R;
import es.sergiopla.freesic.adapters.tabs.Tabsadapter;
import es.sergiopla.freesic.fragment.Friendsfragment;
import es.sergiopla.freesic.helpers.PlayerConfig;
import es.sergiopla.freesic.models.Song;
import es.sergiopla.freesic.tasks.SearchYouTube;

public class MainActivity extends AppCompatActivity implements android.support.v7.app.ActionBar.TabListener {
    public static final String STRING_URL = "https://rss.itunes.apple.com/api/v1/es/itunes-music/top-songs/all/100/non-explicit.json";
    public static final String KEY_LIST = "list";
    public static final String KEY_TITTLE = "tittle";
    public static int listSize, currentSong;

    private ViewPager tabsviewPager;
    private ActionBar mActionBar;
    private Tabsadapter mTabsAdapter;
    private YouTubePlayerView youTubePlayerView;
    private PlayerConfig playerConfig;
    private ImageButton imageButtonNext, imageButtonPlayPause, imageButtonPrevious;
    private ListView listViewSongs;
    private List<Song> songList;
    private SearchYouTube searchYouTube;
    private AdapterView.OnItemClickListener onItemClickListenerList;
    private MainActivity mainActivity;

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
        mTabsAdapter = new Tabsadapter(getSupportFragmentManager());
        tabsviewPager.setAdapter(mTabsAdapter);
        playerConfig = new PlayerConfig(youTubePlayerView);
        imageButtonNext.setEnabled(true);
        imageButtonPrevious.setEnabled(false);

        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getSupportActionBar().setDisplayShowHomeEnabled(false);     // hides action bar icon
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // hides action bar title
        Tab friendstab = getSupportActionBar().newTab().setText("Friends").setTabListener(this);
        Tab publicprofiletab = getSupportActionBar().newTab().setText("Public").setTabListener(this);
        Tab communitytab = getSupportActionBar().newTab().setText("Community").setTabListener(this);

        getSupportActionBar().addTab(friendstab);
        getSupportActionBar().addTab(publicprofiletab);
        getSupportActionBar().addTab(communitytab);

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

        //This helps in providing swiping effect for v7 compat library
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
//                MainActivity.listSize = listViewSongs.getLeft();
//                MainActivity.currentSong = position;
//                searchYouTube = new SearchYouTube(mainActivity, itemTitle, mainActivity);
//                searchYouTube.execute();
                searchVideo(itemTitle, position);
            }
        };
    }

    public void searchVideo(String title, int position) {
        listSize = listViewSongs.getLeft();
        currentSong = position;
        searchYouTube = new SearchYouTube(mainActivity, title, mainActivity);
        searchYouTube.execute();
    }

    public void playVideo(String idVideo) {
        playerConfig = new PlayerConfig(youTubePlayerView);
        playerConfig.loadVideo(idVideo);
    }

    public void playPauseVideo() {
        if (playerConfig.isPlaying()) {
            playerConfig.pauseVideo();
        } else {
            playerConfig.playVideo();
        }
    }

    public void nextVideo() {

    }

    public void setListViewSongs(ListView listViewSongs) {
        this.listViewSongs = listViewSongs;
        this.listViewSongs.setOnItemClickListener(onItemClickListenerList);
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    @Override
    public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTabSelected(Tab selectedtab, FragmentTransaction arg1) {
        // TODO Auto-generated method stub
        int position = selectedtab.getPosition();
        tabsviewPager.setCurrentItem(position); //update tab position on tap
        if (mTabsAdapter.getItem(position) instanceof Friendsfragment) {

        }
    }

    @Override
    public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
        // TODO Auto-generated method stub

    }
}