package oliynick.max.ua.com.test;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRequestIQ;
import xmpp.extensions.com.ua.max.oliynick.iq.ChatListResponceIQ;
import xmpp.extensions.com.ua.max.oliynick.provider.ChatListResponceIQProvider;
import xmpp.extensions.com.ua.max.oliynick.provider.ChatListRetrieveIQProvider;
import xmpp.extensions.com.ua.max.oliynick.util.IQCallback;
import xmpp.extensions.com.ua.max.oliynick.util.UTCUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

/**
 * <p>
 * Tests {@link ChatListRequestIQ}, {@link ChatListResponceIQ} and
 * {@link ChatListResponceIQProvider} classes
 * which are part of <a href="http://xmpp.org/extensions/xep-0136.html">XEP-0136</a>
 * extesion
 * </p>
 * Created 23.02.16
 * @author Max Oliynick
 * */
public final class TestChatListIQ {

    // Test xmpp connection instance
    private static XMPPTCPConnection connection = null;

    /**
     * Initializes connection instance and allocates resources
     * */
    @BeforeClass
    public static void prepareConnection() throws SmackException, IOException, XMPPException {

        // IQ provider registration
        ProviderManager.addIQProvider(ChatListResponceIQProvider.elementName, ChatListResponceIQProvider.namespace, new ChatListResponceIQProvider());
        ProviderManager.addIQProvider(ChatListRetrieveIQProvider.elementName, ChatListRetrieveIQProvider.namespace, new ChatListRetrieveIQProvider());

        connection = new XMPPTCPConnection(TestConfig.buildConfig());
        connection.connect();
        connection.login("maxxx", "qwerty");
    }

    /**
     * <p>
     * Tests server response on GetChatsListIQ
     * class instance
     * </p>
     * @throws NotConnectedException
     * @throws InterruptedException
     * */
    @Test
    public void testResponce() throws NotConnectedException, InterruptedException {

        final long timeout = 3000;
        final IQCallback listener = new IQCallback() {

            // Response stanza
            private Stanza stanza = null;

            // Exception in case of runtime exception
            private Exception exception = null;

            @Override
            public Stanza getStanza() {
                return stanza;
            }

            @Override
            public Exception getException() {
                return exception ;
            }

            @Override
            public synchronized void processPacket(final Stanza stanza) throws NotConnectedException {
                //	response class should be AvailableChatsIQ
                this.stanza = stanza;
                notify();
            }

            @Override
            public synchronized void processException(final Exception exception) {
                this.exception = exception;
                notify();
            }

        };

        final ChatListRequestIQ iq = new ChatListRequestIQ();
        iq.setMax(30);

        connection.sendIqWithResponseCallback(iq, listener, listener, timeout);

        synchronized(listener) {
            listener.wait();

            if(listener.getException() != null) {
                // an error occurred while waiting for response
                fail("Test failed with message: ".concat(listener.getException().getMessage()));
            } else {
                // response should be an instance of AvailableChatsIQ class
                assertTrue("Unknown class", listener.getStanza() instanceof ChatListResponceIQ);

                final ChatListResponceIQ stanza = (ChatListResponceIQ) listener.getStanza();

                System.out.println("\n***************************************************");
                System.out.println("\tTestResponce method\n".concat(stanza.toString()));
                System.out.println("\nXML".concat(stanza.toXML().toString()));
                System.out.println("***************************************************\n");

                assumeTrue("Empty responce set", stanza.getCount() > 0);
                assumeTrue("First index < 0", stanza.getIndex() >= 0);
                assumeTrue("First value == null", stanza.getFirstValue() != null);
                assumeTrue("Last value == null", stanza.getLastValue() != null);

            }
        }
    }

    /**
     * Tests for correct server response
     * parsing on the client side
     * @throws NotConnectedException
     * @throws InterruptedException
     * */
    @Test
    public void testEmptyResponce() throws NotConnectedException, InterruptedException {
        final long timeout = 3000;
        final IQCallback listener = new IQCallback() {

            // Response stanza
            private Stanza stanza = null;

            // Exception in case of runtime exception
            private Exception exception = null;

            @Override
            public Stanza getStanza() {
                return stanza;
            }

            @Override
            public Exception getException() {
                return exception ;
            }

            @Override
            public synchronized void processPacket(final Stanza stanza) throws NotConnectedException {
                //	response class should be AvailableChatsIQ
                this.stanza = stanza;
                notify();
            }

            @Override
            public synchronized void processException(final Exception exception) {
                this.exception = exception;
                notify();
            }

        };

        ChatListRequestIQ iq = new ChatListRequestIQ("non_existing_user@maxlaptop");
        iq.setMax(30);

        connection.sendIqWithResponseCallback(iq, listener, listener, timeout);

        synchronized(listener) {
            listener.wait();

            if(listener.getException() != null) {
                // an error occurred while waiting for response
                fail("Test failed with message: ".concat(listener.getException().getMessage()));
            } else {
                // response should be an instance of AvailableChatsIQ class
                assertTrue("Unknown class", listener.getStanza() instanceof ChatListResponceIQ);

                final ChatListResponceIQ stanza = (ChatListResponceIQ) listener.getStanza();

                System.out.println("\n***************************************************");
                System.out.println("\tTestEmptyResponce method\n".concat(stanza.toString()));
                System.out.println("\nXML".concat(stanza.toXML().toString()));
                System.out.println("***************************************************\n");

                assumeTrue("Not empty responce set", 0 == stanza.getCount());
                assumeTrue("First index >= 0", stanza.getIndex() < 0);
                assumeTrue("First value != null", stanza.getFirstValue() == null);
                assumeTrue("Last value != null", stanza.getLastValue() == null);

            }
        }
    }

    /**
     * <p>Tests getters and setters for correct realization</p>
     * */
    @Test
    public void testGetSet() {

        String with = "test@openfire";
        final String start = "1469-07-21T02:00:00Z";
        final String end = "1479-07-21T04:00:00Z";
        final int max = 100;

        final ChatListRequestIQ iq = new ChatListRequestIQ(with);
        assertEquals(with, iq.getWith());
        assertTrue(iq.getMax() < 0);

        with = "max@openfire";

        assertNotNull("Illegal return value", UTCUtils.fromString(start));

        iq.setMax(max);
        iq.setWith(with);
        iq.setStart(UTCUtils.fromString(start));
        iq.setEnd(end);

        assertEquals(with, iq.getWith());
        assertEquals(max, iq.getMax());
        assertEquals(UTCUtils.fromString(start), iq.getStart());
        assertEquals(end, iq.getEnd());
    }

    /**
     * Closes connection and frees resources
     * */
    @AfterClass
    public static void tearDownConnection() {
        if(connection != null) {
            connection.disconnect();
        } else {
            fail("Connection wasn't set properly!");
        }
    }

}
