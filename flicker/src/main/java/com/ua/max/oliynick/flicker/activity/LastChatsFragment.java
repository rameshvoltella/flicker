package com.ua.max.oliynick.flicker.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.internal.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.inject.Inject;
import com.ua.max.oliynick.flicker.interfaces.ILastChatsModel;
import com.ua.max.oliynick.flicker.util.LastChatModel;
import com.ua.max.oliynick.flicker.util.PresenceUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import oliynick.max.ua.com.flicker.R;
import roboguice.fragment.RoboFragment;

/**
 * Created by Максим on 23.02.2016.
 */
public class LastChatsFragment extends RoboFragment {

    @Inject
    private ILastChatsModel model;

    private ListViewAdapter adapter;

    private class ListViewAdapter extends BaseAdapter {

        private Context context;
        private List<LastChatModel> chatModels;

        public ListViewAdapter(final Context context, final Collection<LastChatModel> items) {
            this.context = context;

            chatModels = new ArrayList<>(items.size() + 1);
            chatModels.addAll(items);
        }

        @Override
        public int getCount() {
            return chatModels.size();
        }

        @Override
        public Object getItem(int position) {
            return chatModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return chatModels.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rowView = convertView;

            if(rowView == null) {
                final LayoutInflater inflater = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                rowView = inflater.inflate(R.layout.last_conv_item, null);
            }

            final TextView nickname = (TextView) rowView.findViewById(R.id.last_conv_nickname);
            final TextView presence = (TextView) rowView.findViewById(R.id.last_conv_presence);
            final TextView message = (TextView) rowView.findViewById(R.id.last_conv_msg);
            final ImageButton conv = (ImageButton) rowView.findViewById(R.id.last_conv_msgBtn);

            final LastChatModel model = chatModels.get(position);

            nickname.setText(model.getLogin());
            presence.setText(PresenceUtils.statusFromPresenceType(model.getPresenceType()));
            message.setText(model.getMessage());

            conv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO join conversation
                }
            });

            return rowView;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        adapter = new ListViewAdapter(getActivity(), model.getLastConversations());

        final View view = inflater.inflate(R.layout.last_conversations, container, false);
        final ListViewCompat contacts = (ListViewCompat) view.findViewById(R.id.lastConvListView);

        contacts.setAdapter(adapter);

        return view;
    }
}
