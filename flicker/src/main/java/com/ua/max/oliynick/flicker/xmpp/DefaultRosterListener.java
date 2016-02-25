package com.ua.max.oliynick.flicker.xmpp;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.RosterListener;

import java.util.Collection;

/**
 * <p>Default implementation of the {@link RosterListener} interface</p>
 * <p>This class does not provide any behavior by default. It just avoids having
 * to implement all the inteface methods if the user is only interested
 * in implementing some of the methods.</p>
 * Created by Максим on 25.02.2016.
 */
public class DefaultRosterListener implements RosterListener {

    @Override
    public void entriesAdded(Collection<String> addresses) {}

    @Override
    public void entriesUpdated(Collection<String> addresses) {}

    @Override
    public void entriesDeleted(Collection<String> addresses) {}

    @Override
    public void presenceChanged(Presence presence) {}

}
