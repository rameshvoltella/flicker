package com.ua.max.oliynick.flicker.activity;

/**
 * Created by Максим on 17.02.2016.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.internal.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.inject.Inject;
import com.ua.max.oliynick.flicker.interfaces.IContactItemController;
import com.ua.max.oliynick.flicker.interfaces.IContactsModel;
import com.ua.max.oliynick.flicker.singleton.MainApp;
import com.ua.max.oliynick.flicker.util.ContactItemModel;
import com.ua.max.oliynick.flicker.util.PresenceUtils;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import oliynick.max.ua.com.flicker.R;
import roboguice.fragment.RoboFragment;

public class ContactFragment extends RoboFragment implements IContactItemController {

    // @InjectView (R.id.contactsListView) ListViewCompat contacts;

    @Inject
    private IContactsModel model;

    private ContactsAdapter adapter = null;

    @Override
    public void onUpdate(final ContactItemModel item) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.updateList(item);
            }
        });
    }

    private class ContactsAdapter extends BaseAdapter {

        private final Context context;
        private List<ContactItemModel> contacts;

        public ContactsAdapter(final Context context, final Collection<ContactItemModel> items) {
            this.context = context;

            contacts = new ArrayList<>(items.size() + 1);
            contacts.addAll(items);
        }

        public void updateList(final ContactItemModel item) {
            if (!contacts.contains(item)) {
                contacts.add(item);
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return contacts.size();
        }

        @Override
        public ContactItemModel getItem(int position) {
            return contacts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return contacts.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final RelativeLayout view = (RelativeLayout)
                    inflater.inflate(R.layout.contact_item, null);

            final TextView nick = (TextView) view.findViewById(R.id.nickname);
            final TextView pres = (TextView) view.findViewById(R.id.presence);
            final ImageButton info = (ImageButton) view.findViewById(R.id.infoBtn);
            final ImageButton msg = (ImageButton) view.findViewById(R.id.msgBtn);

            final ContactItemModel entry = contacts.get(position);

            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO show info fragment
                }
            });

            msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO show message fragment

                    ChatManager cm = ChatManager.getInstanceFor(MainApp.getConnection());
                    Chat ch = cm.createChat(entry.getEntry().getUser());

                    Intent intent = new Intent(context, ChatActivity.class);
                    ChatActivity.setChat(ch);

                    startActivity(intent);
                }
            });

            nick.setText(entry.getEntry().getName());
            pres.setText(PresenceUtils.statusFromPresence(entry.getPresence()));

            return view;
        }
    }

    public ContactFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        model.initialize(null);
        model.setController(this);//FIXME REDO!!

        adapter = new ContactsAdapter(getActivity(), model.getContacts());

        final View view = inflater.inflate(R.layout.contacts, container, false);
        final ListViewCompat contacts = (ListViewCompat) view.findViewById(R.id.contactsListView);

        contacts.setAdapter(adapter);
        return view;
    }

}
