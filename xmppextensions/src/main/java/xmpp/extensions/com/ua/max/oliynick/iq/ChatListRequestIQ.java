package xmpp.extensions.com.ua.max.oliynick.iq;

import org.jivesoftware.smack.packet.IQ;
import org.joda.time.DateTime;

import xmpp.extensions.com.ua.max.oliynick.provider.ChatListResponceIQProvider;
import xmpp.extensions.com.ua.max.oliynick.util.UTCUtils;

/**
 * <p>
 * This IQ stanza should be send in order to get
 * a set of archived conversations
 * </p>
 * @author Max Oliynick
 * */
public final class ChatListRequestIQ extends IQ {

    private String with = null;
    private DateTime start = null;
    private String end = null;
    private int index;
    private int max;

    /**
     * Constructs an empty IQ packet
     * */
    public ChatListRequestIQ() {
        this(null, -1);
    }

    /**
     * Constructs IQ packet with given parameter
     * @param with user's jid
     * */
    public ChatListRequestIQ(final String with) {
        this(with, -1);
    }

    /**
     * Constructs IQ packet with given parameters
     * @param with user's jid
     * @param max max number of conversations to load
     * */
    public ChatListRequestIQ(final String with, int max) {
        super(ChatListResponceIQProvider.elementName, ChatListResponceIQProvider.namespace);
        setWith(with);
        setMax(max);
        index = -1;
    }

    public String getWith() {
        return with;
    }

    public void setWith(String with) {
        this.with = with;
    }

    public DateTime getStart() {
        return start;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(
            IQChildElementXmlStringBuilder xml) {

        if(getWith() != null) xml.attribute("with", getWith());
        if(getStart() != null) xml.attribute("start", UTCUtils.toUTCString(getStart()));
        if(getEnd() != null) xml.attribute("end", getEnd());

        xml.rightAngleBracket();

        if(getMax() >= 0 || getIndex() >= 0) {

            xml.halfOpenElement("set").
                    attribute("xmlns", "http://jabber.org/protocol/rsm").
                    rightAngleBracket();

            if(getMax() >= 0) {
                xml.halfOpenElement("max").
                        rightAngleBracket().append(String.valueOf(getMax())).
                        closeElement("max");
            }

            if(getIndex() >= 0) {
                xml.halfOpenElement("index").
                        rightAngleBracket().append(String.valueOf(getIndex())).
                        closeElement("index");
            }

            xml.closeElement("set");

        }

        return xml;
    }

    @Override
    public String toString() {
        return "ChatListRequestIQ [with=" + with + ", start=" + start
                + ", end=" + end + ", max=" + max + "]";
    }

}
