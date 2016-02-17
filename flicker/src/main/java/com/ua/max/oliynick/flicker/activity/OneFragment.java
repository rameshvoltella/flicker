package com.ua.max.oliynick.flicker.activity;

/**
 * Created by Максим on 17.02.2016.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ua.max.oliynick.flicker.model.MainModel;

import org.jivesoftware.smack.roster.RosterEntry;

import oliynick.max.ua.com.flicker.R;

public class OneFragment extends Fragment {

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment_one for this fragment

        MainModel model = new MainModel(getActivity());

        View view = inflater.inflate(R.layout.fragment_one, container, false);

        ListView contacts = (ListView) view.findViewById(R.id.contactsListView);


        contacts.setAdapter(new MySimpleArrayAdapter(getActivity(), model.entries()));

        return view;
    }

    class MySimpleArrayAdapter extends ArrayAdapter<RosterEntry> {
        private final Context context;
        private final RosterEntry[] values;

        public MySimpleArrayAdapter(Context context, RosterEntry[] values) {
            super(context, R.layout.contact_item, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.contact_item, null);
            TextView nick = (TextView) view.findViewById(R.id.nickname);
            TextView pres = (TextView) view.findViewById(R.id.presence);

            RosterEntry entry = values[position];

            nick.setText(entry.getName());
            //pres.setText(roster.getPresence(entry.getUser()).getStatus());

            return view;
        }
    }

}
