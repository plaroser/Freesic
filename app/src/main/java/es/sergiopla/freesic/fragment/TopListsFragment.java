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
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.sergiopla.freesic.R;
import es.sergiopla.freesic.adapters.lists.AdapterListMusic;
import es.sergiopla.freesic.adapters.tabs.Tabsadapter;
import es.sergiopla.freesic.views.MainActivity;

public class TopListsFragment extends Fragment {
    private AdapterListMusic adapterListMusic;
    private Context context;
    private ListView listView;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        mainActivity = ((MainActivity) getActivity());
        View view = inflater.inflate(R.layout.top_lists_fragment_view, container, false);
        listView = view.findViewById(R.id.ListViewLists);
        final List<String> listOfList = new ArrayList<>();
        for (Map.Entry<String, String> entry : MainActivity.getListItunes().entrySet()) {
            String key = entry.getKey();
            listOfList.add(key);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mainActivity.cargarLista(MainActivity.getListItunes().get(listOfList.get(position)));
                mainActivity.changeTab(Tabsadapter.INDEX_FRIENDS_FRAGMENT);
            }
        });
        adapterListMusic = new AdapterListMusic(context, R.id.ListViewLists, listOfList);
        listView.setAdapter(adapterListMusic);
        return view;
    }
}