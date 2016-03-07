package xmpp.extensions.com.ua.max.oliynick.service;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Stanza;

import java.util.Map;
import java.util.WeakHashMap;

import xmpp.extensions.com.ua.max.oliynick.interfaces.AbstractArchiveManager;
import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRequestIQ;
import xmpp.extensions.com.ua.max.oliynick.iq.ChatListResponceIQ;
import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRetrieveRequestIQ;
import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRetrieveResponceIQ;
import xmpp.extensions.com.ua.max.oliynick.service.entity.ArchivedChatPage;
import xmpp.extensions.com.ua.max.oliynick.service.entity.ArchivedMessagePage;
import xmpp.extensions.com.ua.max.oliynick.util.DefaultIQCallback;
import xmpp.extensions.com.ua.max.oliynick.util.IQCallback;

/**
 * <p>
 * Default implementation of {@linkplain AbstractArchiveManager}
 * </p>
 * @author Max Oliynick
 * */
public class ArchiveManager extends AbstractArchiveManager {

    private static final Map<XMPPConnection, ArchiveManager> instances;

    private static long awaitTimeout = 3000;

    static {
        instances = new WeakHashMap<>(3);
    }

    public static long getAwaitTimeout() {
        return awaitTimeout;
    }

    public static void setAwaitTimeout(long awaitTimeout) {
        ArchiveManager.awaitTimeout = awaitTimeout;
    }

    private final XMPPConnection connection;

    private ArchiveManager(final XMPPConnection connection) {
        super();

        this.connection = connection;
        // stanza listener registration
        this.connection.addAsyncStanzaListener(new StanzaListener() {

            @Override
            public void processPacket(Stanza packet)
                    throws NotConnectedException {
                doProcessStanza(packet);
            }
        }, new StanzaFilter() {

            @Override
            public boolean accept(Stanza stanza) {
                return stanza instanceof ChatListResponceIQ
                        || stanza instanceof ChatListRetrieveResponceIQ;
            }
        });

    }

    public static synchronized ArchiveManager getInstanceFor(final XMPPConnection connection) {

        if(!instances.containsKey(connection)) {
            instances.put(connection, new ArchiveManager(connection));
        }

        return instances.get(connection);
    }

    @Override
    public ArchivedChatPage retreiveArchivedChats(final ChatListRequestIQ iq) throws NotConnectedException {

        if(iq == null)
            throw new NullPointerException("iq == null");

        final IQCallback callback = new DefaultIQCallback();
        connection.sendIqWithResponseCallback(iq, callback, callback, getAwaitTimeout());

        synchronized(callback) {

            try {
                callback.wait();

                if(callback.getException() != null)
                    throw callback.getException();

                return new ArchivedChatPage((ChatListResponceIQ) callback.getStanza());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    public ArchivedChatPage retreiveArchivedChats() throws NotConnectedException {
        return retreiveArchivedChats(new ChatListRequestIQ());
    }

    @Override
    public ArchivedMessagePage retreiveArchivedMessages(ChatListRetrieveRequestIQ iq) throws NotConnectedException {

        final IQCallback callback = new DefaultIQCallback();
        connection.sendIqWithResponseCallback(iq, callback, callback, getAwaitTimeout());

        synchronized(callback) {

            try {
                callback.wait();

                if(callback.getException() != null)
                    throw callback.getException();

                return new ArchivedMessagePage((ChatListRetrieveResponceIQ) callback.getStanza());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    public void sendIQ(IQ iq) throws Exception {
        connection.sendStanza(iq);
    }

    private void doProcessStanza(final Stanza stanza) {

        if(stanza instanceof ChatListResponceIQ) {
            fireArchChatListeners((ChatListResponceIQ) stanza);

        } else if(stanza instanceof ChatListRetrieveResponceIQ) {
            fireArchMsgListeners((ChatListRetrieveResponceIQ) stanza);

        }
    }

}
