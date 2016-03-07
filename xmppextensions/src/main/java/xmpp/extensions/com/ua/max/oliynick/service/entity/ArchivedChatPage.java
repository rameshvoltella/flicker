package xmpp.extensions.com.ua.max.oliynick.service.entity;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import xmpp.extensions.com.ua.max.oliynick.iq.ChatListResponceIQ;
import xmpp.extensions.com.ua.max.oliynick.util.Tuple;

/**
 * <p>
 * Represents retrieved from server response page for
 * {@link ChatListResponceIQ}
 * </p>
 * @author Max Oliynick
 * */
public final class ArchivedChatPage {

    private final List<xmpp.extensions.com.ua.max.oliynick.service.entity.ArchivedChat> chats;

    private final int index;

    private final String firstValue, lastValue;

    private final ChatListResponceIQ iq;

    public ArchivedChatPage(final ChatListResponceIQ iq) {

        if(iq == null)
            throw new NullPointerException("iq == null");

        chats = new ArrayList<>(iq.getCount() > 1 ? iq.getCount() : 10);

        for(final Tuple<String, DateTime> chat : iq.getChats()) {
            chats.add(new xmpp.extensions.com.ua.max.oliynick.service.entity.ArchivedChat(chat.getFirst(), chat.getSecond()));
        }

        this.firstValue = iq.getFirstValue();
        this.lastValue = iq.getLastValue();
        this.index = iq.getIndex();
        this.iq = iq;
    }

    public int getIndex() {
        return index;
    }

    public String getFirstValue() {
        return firstValue;
    }

    public String getLastValue() {
        return lastValue;
    }

    public int getCount() {
        return iq.getCount();
    }

    public Collection<xmpp.extensions.com.ua.max.oliynick.service.entity.ArchivedChat> getChats() {
        return Collections.unmodifiableCollection(chats);
    }

    public String toXml() {
        return iq.toXML().toString();
    }

    @Override
    public int hashCode() {
        final int prime = 101;
        int result = 1;
        result = prime * result + ((chats == null) ? 0 : chats.hashCode());
        result = prime * result
                + ((firstValue == null) ? 0 : firstValue.hashCode());
        result = prime * result + index;
        result = prime * result
                + ((lastValue == null) ? 0 : lastValue.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ArchivedChatPage other = (ArchivedChatPage) obj;
        if (chats == null) {
            if (other.chats != null) return false;
        } else if (!chats.equals(other.chats)) return false;
        if (firstValue == null) {
            if (other.firstValue != null) return false;
        } else if (!firstValue.equals(other.firstValue)) return false;
        if (index != other.index) return false;
        if (lastValue == null) {
            if (other.lastValue != null) return false;
        } else if (!lastValue.equals(other.lastValue)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ArchivedChatPage [iq=" + iq + "]";
    }

}
