package com.ua.max.oliynick.flicker.model;

import android.content.Context;
import android.util.Log;

import com.ua.max.oliynick.flicker.interfaces.IContactsModel;
import com.ua.max.oliynick.flicker.util.ContactItemModel;
import com.ua.max.oliynick.flicker.util.StringUtils;
import com.ua.max.oliynick.flicker.util.XMPPTCPConnectionHolder;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import roboguice.inject.ContextSingleton;

/**
 * Created by Максим on 17.02.2016.
 */
@ContextSingleton
public class ContactModel extends IContactsModel implements RosterListener {

    private Roster roster = null;

    private Map<String, ContactItemModel> contacts = null;

    public ContactModel(){}

    @Override
    public void initialize(Context context) {
        init();
    }

    @Override
    public Collection<ContactItemModel> getContacts() {
        return contacts.values();
    }

    @Override
    public Collection<ContactItemModel> getContacts(String query) {
        return null;
    }

    @Override
    public void showInfo() {

    }

    @Override
    public void showConversation() {

    }

    @Override
    protected void onContactListUpdate(ContactItemModel item) {
        getController().onUpdate(item);
    }

    @Override
    public void entriesAdded(Collection<String> addresses) {
        Log.i("ContactModel", "entriesAdded " + addresses.toString());
    }

    @Override
    public void entriesUpdated(Collection<String> addresses) {
        Log.i("ContactModel", "entriesUpdated " + addresses.toString());
    }

    @Override
    public void entriesDeleted(Collection<String> addresses) {
        Log.i("ContactModel", "entriesDeleted " + addresses.toString());
    }

    @Override
    public void presenceChanged(Presence presence) {

        final String key = StringUtils.parseBareJid(presence.getFrom());
        final Presence highPriorPres = roster.getPresence(presence.getFrom());
        ContactItemModel item = contacts.get(key);

        RosterEntry entry = null;


        if(item != null) {
            item.setPresence(highPriorPres);
        } else if((entry = roster.getEntry(presence.getFrom())) != null) {
            item = new ContactItemModel(entry, highPriorPres);
            contacts.put(key, item);
        }

        onContactListUpdate(item);
    }

    private void init() {

        if(XMPPTCPConnectionHolder.getInstance() == null)
            throw new RuntimeException("Xmpp connection is null");

        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
        roster = Roster.getInstanceFor(XMPPTCPConnectionHolder.getInstance());
        roster.setSubscriptionMode(Roster.SubscriptionMode.manual);

        contacts = new HashMap<>(roster.getEntryCount() + 1);

        for(final RosterEntry entry : roster.getEntries()) {

            final Presence presence = roster.getPresence(entry.getUser());
            final String key = StringUtils.parseBareJid(entry.getUser());

            contacts.put(key, new ContactItemModel(entry, presence));
        }

        roster.addRosterListener(this);
    }
}
