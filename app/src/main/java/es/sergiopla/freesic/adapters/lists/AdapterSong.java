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
import es.sergiopla.freesic.models.Song;


public class AdapterSong extends ArrayAdapter<Song> {
    private List<Song> songs;

    public AdapterSong(@NonNull Context context, int resource, List<Song> songs) {
        super(context, resource);
        this.songs = songs;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
//        if (convertView == null)
        convertView = newView();

        // Store is the type of this ArrayAdapter
        Song song = getItem(position);
        Holder h = (Holder) convertView.getTag();

        // And here I get the data and address them to the UI
        // as you can see, if the convertView is not null,
        // I'm not creating a new one, I'm just changing text, images, etc
        h.textViewNum.setText(String.valueOf(position + 1));
        h.textViewTittle.setText(song.getTitle());
        h.textViewAuthor.setText(song.getArtist());
        return convertView;
    }

    @Override
    public void add(@Nullable Song object) {
        super.add(object);
    }

    @Nullable
    @Override
    public Song getItem(int position) {
        return songs.get(position);
    }

    @Override
    public int getCount() {
        return songs.size();
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
        View v = inf.inflate(R.layout.song_item, null);
        Holder h = new Holder();
        // here we store that holder inside the view itself
        v.setTag(h);

        // and only call those findById on this first start
        h.textViewAuthor = v.findViewById(R.id.textViewAuthor);
        h.textViewTittle = v.findViewById(R.id.textViewTittle);
        h.textViewNum = v.findViewById(R.id.textViewNum);
        return v;
    }

    // this class is here just to hold reference to the UI elements
    // findViewById is a lengthy operation so this is one of the optimisations
    private class Holder {
        TextView textViewTittle;
        TextView textViewAuthor;
        TextView textViewNum;
    }
}
