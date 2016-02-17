package com.ua.max.oliynick.flicker.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ua.max.oliynick.flicker.util.XMPPTCPConnectionHolder;

import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.List;

import oliynick.max.ua.com.flicker.R;

/**
 * Created by Максим on 17.02.2016.
 */
public class MainModel {

    private Roster roster;
    private LayoutInflater inflater;

    private List<View> contactsList = null;

    public MainModel(final Context context){

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
        roster = Roster.getInstanceFor(XMPPTCPConnectionHolder.getInstance());
        roster.setSubscriptionMode(Roster.SubscriptionMode.manual);

        contactsList = new ArrayList<>(roster.getEntryCount());

        for(final RosterEntry entry : roster.getEntries()) {

            if(entry.getStatus() != null)
            Log.i("roster", entry.getStatus().toString());

            if(entry.getType() != null)
            Log.i("roster", entry.getType().toString());

            //contactsList.add(fromEntry(entry));
        }

    }

    public RosterEntry [] entries() {
        return roster.getEntries().toArray(new RosterEntry[roster.getEntryCount()]);
    }

    public List<View> getList() {
        return contactsList;
    }

    private View fromEntry(final RosterEntry entry) {

        View view = inflater.inflate(R.layout.contact_item, null);
        TextView nick = (TextView) view.findViewById(R.id.nickname);
        TextView pres = (TextView) view.findViewById(R.id.presence);

        nick.setText(entry.getName());
        pres.setText(roster.getPresence(entry.getUser()).getStatus());

        return view;
    }

}
