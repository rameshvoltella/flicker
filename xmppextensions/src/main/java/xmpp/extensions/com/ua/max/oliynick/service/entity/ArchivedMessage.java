package xmpp.extensions.com.ua.max.oliynick.service.entity;

import org.joda.time.DateTime;

import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRequestIQ;
import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRetrieveResponceIQ.Direction;
import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRetrieveResponceIQ.WrappedBody;

/**
 * <p>Represents archived message entry for {@link ChatListRequestIQ}</p>
 * @author Max Oliynick
 * */
public class ArchivedMessage implements Comparable<ArchivedMessage> {

    private final int secs;
    private final DateTime fullSecs;
    private final String body;
    private final Direction direction;

    public ArchivedMessage(final WrappedBody body) {
        this.secs = body.getChatSecs();
        this.fullSecs = body.getSecs();
        this.body = body.getBody();
        this.direction = body.getDirection();
    }

    public DateTime getFullSecs() {
        return fullSecs;
    }

    public String getMessageBody() {
        return body;
    }

    public int getSecondsCreation() {
        return secs;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public int compareTo(ArchivedMessage o) {
        return getFullSecs().compareTo(o.getFullSecs());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((body == null) ? 0 : body.hashCode());
        result = prime * result
                + ((direction == null) ? 0 : direction.hashCode());
        result = prime * result + secs;
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
        ArchivedMessage other = (ArchivedMessage) obj;
        if (body == null) {
            if (other.body != null)
                return false;
        } else if (!body.equals(other.body))
            return false;
        if (direction != other.direction)
            return false;
        if (secs != other.secs)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ArchivedMessage [secs=" + secs + ", body=" + body
                + ", direction=" + direction + "]";
    }

}
