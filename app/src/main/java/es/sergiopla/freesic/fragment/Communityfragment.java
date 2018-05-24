package es.sergiopla.freesic.fragment;

/**
 * Created by Sergio on 5/24/2018.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.sergiopla.freesic.R;


public class Communityfragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.communityview, container, false);

        return view;
    }
}