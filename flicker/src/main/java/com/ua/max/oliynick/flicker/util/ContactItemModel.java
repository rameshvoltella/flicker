package com.ua.max.oliynick.flicker.util;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.RosterEntry;

/**
 * Created by Максим on 18.02.2016.
 */
public class ContactItemModel {

    private Presence presence;

    private final RosterEntry entry;

    public ContactItemModel(final RosterEntry entry, final Presence presence) {
        this.entry = entry;
        this.presence = presence;
    }

    public Presence getPresence() {
        return presence;
    }

    public void setPresence(Presence presence) {
        this.presence = presence;
    }

    public RosterEntry getEntry() {
        return entry;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!(o instanceof ContactItemModel)) return false;
        ContactItemModel another = (ContactItemModel) o;
        return another.hashCode() == hashCode() && another.entry.equalsDeep(entry);
    }

    @Override
    public int hashCode() {
        return entry.hashCode();
    }
}
