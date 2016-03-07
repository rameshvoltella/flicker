package com.ua.max.oliynick.flicker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.internal.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.ua.max.oliynick.flicker.interfaces.ILastChatsModel;
import com.ua.max.oliynick.flicker.util.GenericObserver;
import com.ua.max.oliynick.flicker.util.LastChatModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

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

        public ListViewAdapter(final Context context) {
            this.context = context;
            chatModels = new ArrayList<>();
        }

        public ListViewAdapter(final Context context, final Collection<LastChatModel> items) {
            this.context = context;
            chatModels = new ArrayList<>(items.size() + 1);
            chatModels.addAll(items);
        }

        public synchronized void insert(final LastChatModel newModel) {

            final Iterator<LastChatModel> it = chatModels.iterator();

            while (it.hasNext()) {
                final LastChatModel model = it.next();

                if (model.equals(newModel)) {

                    model.setPresenceType(newModel.getPresenceType());

                    if (newModel.getMessage() != null &&
                            newModel.getMessage().length() > 0) {

                        model.setMessage(newModel.getMessage());

                        it.remove();
                        chatModels.add(0, model);
                        notifyDataSetChanged();
                    }
                    return;
                }
            }

            chatModels.add(0, newModel);
            notifyDataSetChanged();
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

            if (rowView == null) {
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
            //presence.setText(PresenceUtils.statusFromPresenceType(model.getPresenceType()));
            message.setText(model.getMessage());

            conv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO join conversation
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra(ChatActivity.JID_KEY, model.getJid());
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
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

        try {
            adapter = new ListViewAdapter(getActivity(), model.loadNextLastConv());
        } catch (Exception e) {
            e.printStackTrace();
            adapter = new ListViewAdapter(getActivity());
            Toast.makeText(getActivity(), "failed to load initial conversations", Toast.LENGTH_LONG);
        }

        final View view = inflater.inflate(R.layout.last_conversations, container, false);
        final ListViewCompat contacts = (ListViewCompat) view.findViewById(R.id.lastConvListView);

        contacts.setAdapter(adapter);

        model.addChatUpdateObserver(new GenericObserver<LastChatModel>() {

            @Override
            public void onValueChanged(final Observable observable,
                                       final LastChatModel oldValue,
                                       final LastChatModel newValue) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.insert(newValue);
                    }
                });
            }
        });

        return view;
    }

}
