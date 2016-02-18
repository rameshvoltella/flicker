package com.ua.max.oliynick.flicker.util;

import org.jivesoftware.smack.packet.Presence;

/**
 * Created by Максим on 18.02.2016.
 */
public class PresenceUtils {

    /**
     * Returns current status of given presence in terms
     * of message 'online' or 'offline'
     * @throws IllegalArgumentException if presences is null
     * */
    public static String statusFromPresence(final Presence presence) {

        if(presence == null)
            throw new IllegalArgumentException("presence == null");

        if(presence.getType() == Presence.Type.unavailable) return "Offline";
        return "Online";
    }

}
