package es.sergiopla.freesic.adapters.tabs;

/**
 * Created by Sergio on 5/24/2018.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import es.sergiopla.freesic.fragment.SearchFragment;
import es.sergiopla.freesic.fragment.TopListsFragment;
import es.sergiopla.freesic.fragment.ViewListFragment;

public class Tabsadapter extends FragmentStatePagerAdapter {

    public static final int INDEX_COMMUNITY_FRAGMENT = 0;
    public static final int INDEX_FRIENDS_FRAGMENT = 1;
    public static final int INDEX_PUBLIC_PROFILE_FRAGMENT = 2;

    private int TOTAL_TABS;
    private Fragment[] fragments;

    public Tabsadapter(FragmentManager fm) {
        super(fm);
        fragments = new Fragment[]{
                new TopListsFragment(),
                new ViewListFragment(),
                new SearchFragment()};
        TOTAL_TABS = fragments.length;
    }

    @Override
    public Fragment getItem(int index) {
        if (index >= 0 && index < TOTAL_TABS)
            return fragments[index];
        return null;
    }

    @Override
    public int getCount() {
        return TOTAL_TABS;
    }
}