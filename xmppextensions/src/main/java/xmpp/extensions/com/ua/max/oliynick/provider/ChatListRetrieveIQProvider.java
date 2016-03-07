package xmpp.extensions.com.ua.max.oliynick.provider;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRetrieveResponceIQ;
import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRetrieveResponceIQ.Direction;
import xmpp.extensions.com.ua.max.oliynick.util.UTCUtils;

/**
 * <p>
 * Custom provider that parsers XEP-0136 result history into
 * {@link ChatListRetrieveResponceIQ} packets
 * </p>
 * @author Max Oliynick
 * */
public final class ChatListRetrieveIQProvider extends IQProvider<ChatListRetrieveResponceIQ> {

    public static final String namespace = "urn:xmpp:archive";
    public static final String elementName = "chat";

    private static final String chatTag = "chat";
    private static final String fromTag = "from";
    private static final String toTag = "to";
    private static final String firstTag = "first";
    private static final String lastTag = "last";
    private static final String countTag = "count";

    private static final String withAttr = "with";
    private static final String startAttr = "start";
    private static final String subjectAttr = "subject";
    private static final String versionAttr = "version";

    public ChatListRetrieveIQProvider() {}

    @Override
    public ChatListRetrieveResponceIQ parse(XmlPullParser parser, int depth)
            throws XmlPullParserException, IOException, SmackException {

        final ChatListRetrieveResponceIQ iq = new ChatListRetrieveResponceIQ();

        do {

            final String name = parser.getName();

            switch (parser.getEventType()) {
                case XmlPullParser.START_TAG : {

                    if (name.equals(chatTag)) {
                        parseRetrTagAttrs(iq, parser, parser.getAttributeCount());

                    } else if(name.equals(fromTag) || name.equals(toTag)) {
                        final int secs = Integer.valueOf(parser.getAttributeValue(0));
                        final Direction direction = name.equals(fromTag) ? Direction.from : Direction.to;

                        parser.nextTag();
                        iq.addBody(direction, secs, parser.nextText());

                    } else if(name.equals(firstTag)) {
                        iq.setFirstIndex(Integer.valueOf(parser.getAttributeValue(0)));
                        iq.setFirstValue(parser.nextText());

                    } else if(name.equals(lastTag)) {
                        iq.setLastValue(parser.nextText());

                    } else if(name.equals(countTag)) {
                        iq.setCount(Integer.valueOf(parser.nextText()));
                    }

                    break;
                }
            }

            parser.next();

        } while(parser.getDepth() != depth);

        return iq;

    }

    /**
     * Parses packet attributes
     * */
    private void parseRetrTagAttrs(ChatListRetrieveResponceIQ iq, XmlPullParser parser, int count) {

        for(int i = 0; i < count; ++i) {
            if(parser.getAttributeName(i).equals(withAttr)) {
                iq.setWith(parser.getAttributeValue(i));

            } else if(parser.getAttributeName(i).equals(startAttr)) {
                iq.setStart(UTCUtils.fromString(parser.getAttributeValue(i)));

            } else if(parser.getAttributeName(i).equals(subjectAttr)) {
                iq.setSubject(parser.getAttributeValue(i));

            } else if(parser.getAttributeName(i).equals(versionAttr)) {
                iq.setVersion(Integer.valueOf(parser.getAttributeValue(i)));
            }
        }
    }

}
