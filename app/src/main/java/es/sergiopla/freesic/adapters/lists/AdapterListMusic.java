package es.sergiopla.freesic.adapters.lists;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import es.sergiopla.freesic.R;

public class AdapterListMusic extends ArrayAdapter<String> {
    private List<String> listMusic;

    public AdapterListMusic(@NonNull Context context, int resource, List<String> listMusic) {
        super(context, resource);
        this.listMusic = listMusic;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
//        if (convertView == null)
        convertView = newView();

        // Store is the type of this ArrayAdapter
        String item = getItem(position);
        Holder h = (Holder) convertView.getTag();

        // And here I get the data and address them to the UI
        // as you can see, if the convertView is not null,
        // I'm not creating a new one, I'm just changing text, images, etc
        h.textViewTittle.setText(item);
        return convertView;
    }

    @Override
    public void add(@Nullable String object) {
        super.add(object);
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return listMusic.get(position);
    }

    @Override
    public int getCount() {
        return listMusic.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private View newView() {
        LayoutInflater inf = LayoutInflater.from(getContext());
        View v = inf.inflate(R.layout.list_music_item, null);
        Holder h = new Holder();
        v.setTag(h);

        h.textViewTittle = v.findViewById(R.id.textViewListTitle);
        return v;
    }

    private class Holder {
        TextView textViewTittle;
    }
}
