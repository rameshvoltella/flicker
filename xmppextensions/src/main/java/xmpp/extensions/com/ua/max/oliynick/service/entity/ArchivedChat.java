package xmpp.extensions.com.ua.max.oliynick.service.entity;

import org.joda.time.DateTime;

import xmpp.extensions.com.ua.max.oliynick.iq.ChatListResponceIQ;

/**
 * <p>Represents archived chat entry for {@link ChatListResponceIQ}</p>
 * @author Max Oliynick
 * */
public class ArchivedChat {

    private final String with;

    private final DateTime start;

    public ArchivedChat(final String with, final DateTime start) {
        if(with == null)
            throw new NullPointerException("with == null");

        if(start == null)
            throw new NullPointerException("start == null");
        this.with = with;
        this.start = start;
    }

    public String getWith() {
        return with;
    }

    public DateTime getStart() {
        return start;
    }

    @Override
    public int hashCode() {
        final int prime = 33;
        int result = 1;
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        result = prime * result + ((with == null) ? 0 : with.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ArchivedChat other = (ArchivedChat) obj;
        if (start == null) {
            if (other.start != null)
                return false;
        } else if (!start.equals(other.start))
            return false;
        if (with == null) {
            if (other.with != null)
                return false;
        } else if (!with.equals(other.with))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ArchivedChat [with=" + with + ", start=" + start + "]";
    }

}
