package com.asocialfingers.mp_list.Backend.Adapters.Version;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.asocialfingers.mp_list.R;

public class CustomVersionNotesAdapter extends BaseAdapter {

    LayoutInflater inflter;
    Context context;
    String Header[], Description[];

    public CustomVersionNotesAdapter(Context applicationContext, String[] header, String[] description) {
        this.context = context;
        this.Header = header;
        this.Description = description;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return Header.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_list_view_version_notes, null);
        TextView headers = (TextView) view.findViewById(R.id.lblHeader);
        TextView descriptions = (TextView) view.findViewById(R.id.lblDescription);

        headers.setText(Header[i]);
        descriptions.setText(Description[i]);

        return view;
    }
}
