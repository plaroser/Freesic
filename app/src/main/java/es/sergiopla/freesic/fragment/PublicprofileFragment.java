package es.sergiopla.freesic.fragment;

/**
 * Created by Sergio on 5/24/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import es.sergiopla.freesic.R;
import es.sergiopla.freesic.adapters.tabs.Tabsadapter;
import es.sergiopla.freesic.views.MainActivity;


public class PublicprofileFragment extends Fragment {
    private static final String[] URL_SEARCH = {"https://itunes.apple.com/search?term=", "&entity=song"};

    private MainActivity mainActivity;
    private ImageButton imageButtonSearch;
    private EditText editTextSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //  ___       _ _   _       _ _          _   _
        // |_ _|_ __ (_) |_(_) __ _| (_)______ _| |_(_) ___  _ __
        //  | || '_ \| | __| |/ _` | | |_  / _` | __| |/ _ \| '_ \
        //  | || | | | | |_| | (_| | | |/ / (_| | |_| | (_) | | | |
        // |___|_| |_|_|\__|_|\__,_|_|_/___\__,_|\__|_|\___/|_| |_|
        View view = inflater.inflate(R.layout.publicprofileview, container, false);
        mainActivity = ((MainActivity) getActivity());
        imageButtonSearch = view.findViewById(R.id.imageButtonSearch);
        imageButtonSearch.setEnabled(false);
        editTextSearch = view.findViewById(R.id.editTextSearch);

        editTextSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                String temporalText = editTextSearch.getText().toString();
                imageButtonSearch.setEnabled(!temporalText.isEmpty());
                //Whenever you got user click enter. Get text in edittext and check it equal test1.
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    search();
                }
                return false;
            }
        });

        imageButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        return view;
    }

    private String makeURLSearch(String query) {
        if (!query.isEmpty()) {
            query = query.trim().replaceAll("[^a-zA-Z0-9 ]+", "").replaceAll(" +", " ").toLowerCase().replaceAll(" ", "+");
            query = URL_SEARCH[0] + query + URL_SEARCH[1];
            Log.v(MainActivity.LOG_ID, "Query generada: " + query);

            return query;
        }
        return null;
    }

    private void search() {
        hideKeyboardFrom(mainActivity, editTextSearch);
        String temporalText = editTextSearch.getText().toString();
        String url = makeURLSearch(temporalText);
        mainActivity.cargarLista(url);
        mainActivity.changeTab(Tabsadapter.INDEX_FRIENDS_FRAGMENT);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}