package es.sergiopla.freesic.views;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

import es.sergiopla.freesic.R;
import es.sergiopla.freesic.adapters.tabs.Tabsadapter;
import es.sergiopla.freesic.helpers.PlayerConfig;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabsviewPager = findViewById(R.id.tabspager);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        imageButtonPrevious = findViewById(R.id.imageButtonPrevious);
        imageButtonPlayPause = findViewById(R.id.imageButtonPlayPause);
        imageButtonNext = findViewById(R.id.imageButtonNext);
        mTabsAdapter = new Tabsadapter(getSupportFragmentManager());
        tabsviewPager.setAdapter(mTabsAdapter);
        playerConfig = new PlayerConfig(youTubePlayerView, this);
        imageButtonNext.setEnabled(false);
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

    }

    public void playVideo(String idVideo) {
        playerConfig.loadVideo(idVideo);
    }

    public void playPauseVideo() {
        if (playerConfig.isPlaying()) {
            playerConfig.pauseVideo();
        } else {
            playerConfig.playVideo();
        }
    }

    @Override
    public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTabSelected(Tab selectedtab, FragmentTransaction arg1) {
        // TODO Auto-generated method stub
        tabsviewPager.setCurrentItem(selectedtab.getPosition()); //update tab position on tap
    }

    @Override
    public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
        // TODO Auto-generated method stub

    }
}