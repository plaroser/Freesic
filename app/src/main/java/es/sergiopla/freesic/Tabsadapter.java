package es.sergiopla.freesic;

/**
 * Created by Sergio on 5/24/2018.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class Tabsadapter  extends FragmentStatePagerAdapter{

    private int TOTAL_TABS = 3;

    public Tabsadapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int index) {
        // TODO Auto-generated method stub
        switch (index) {
            case 0:
                return new Friendsfragment();

            case 1:
                return new PublicprofileFragment();

            case 2:
                return new Communityfragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return TOTAL_TABS;
    }

}