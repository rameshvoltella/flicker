package xmpp.extensions.com.ua.max.oliynick.service.entity;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRetrieveRequestIQ;
import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRetrieveResponceIQ;
import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRetrieveResponceIQ.WrappedBody;

/**
 * <p>
 * Represents retrieved from server response page for
 * {@link ChatListRetrieveRequestIQ}
 * </p>
 * @author Max Oliynick
 * */
public class ArchivedMessagePage {

    private final ChatListRetrieveResponceIQ iq;

    private final List<ArchivedMessage> messages;

    public ArchivedMessagePage(final ChatListRetrieveResponceIQ iq) {

        if(iq == null)
            throw new NullPointerException("iq == null");

        this.iq = iq;

        this.messages = new ArrayList<>(iq.getCount() + 1);

        for(final WrappedBody msgBody : iq.getContent()) {
            messages.add(new ArchivedMessage(msgBody));
        }

    }

    public String getStanzaID() {
        return iq.getStanzaId();
    }

    public String getWith() {
        return iq.getWith();
    }

    public DateTime getStartDate() {
        return iq.getStart();
    }

    public int getIndex() {
        return iq.getIndex();
    }

    public String getFirstValue() {
        return iq.getFirstValue();
    }

    public String getLastValue() {
        return iq.getLastValue();
    }

    public String getSubject() {
        return iq.getSubject();
    }

    public int getVersion() {
        return iq.getVersion();
    }

    public List<ArchivedMessage> getMessages() {
        return messages;
    }

    public String toXml() {
        return iq.toXML().toString();
    }

    @Override
    public String toString() {
        return "ArchivedMessagePage [iq=" + iq + "]";
    }

}
