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

import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRetrieveRequestIQ;
import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRetrieveResponceIQ;
import xmpp.extensions.com.ua.max.oliynick.provider.ChatListResponceIQProvider;
import xmpp.extensions.com.ua.max.oliynick.provider.ChatListRetrieveIQProvider;
import xmpp.extensions.com.ua.max.oliynick.util.IQCallback;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

/**
 * <p>
 * Tests {@link ChatListRetrieveRequestIQ}, {@link ChatListRetrieveResponceIQ}
 * and {@link ChatListRetrieveIQProvider} classes
 * which are part of <a href="http://xmpp.org/extensions/xep-0136.html">XEP-0136</a>
 * extesion
 * </p>
 * Created 02.03.16
 * @author Max Oliynick
 * */
public final class TestChatListRetrieveIQ {

    // Test xmpp connection instance
    private static XMPPTCPConnection connection = null;

    /**
     * Initializes connection instance and allocates resources
     * */
    @BeforeClass
    public static void prepareConnection() throws SmackException, IOException,
            XMPPException {

        // IQ provider registration
        ProviderManager.addIQProvider(ChatListResponceIQProvider.elementName,
                ChatListResponceIQProvider.namespace,
                new ChatListResponceIQProvider());
        ProviderManager.addIQProvider(ChatListRetrieveIQProvider.elementName,
                ChatListRetrieveIQProvider.namespace,
                new ChatListRetrieveIQProvider());

        connection = new XMPPTCPConnection(TestConfig.buildConfig());
        connection.connect();
        connection.login("maxxx", "qwerty");
    }

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

        final ChatListRetrieveRequestIQ iq = new ChatListRetrieveRequestIQ("mary@maxlaptop");
        iq.setMax(30);

        connection.sendIqWithResponseCallback(iq, listener, listener, timeout);

        synchronized(listener) {
            listener.wait();

            if(listener.getException() != null) {
                // an error occurred while waiting for response
                fail("Test failed with message: ".concat(listener.getException().getMessage()));
            } else {
                // response should be an instance of AvailableChatsIQ class
                assertTrue("Unknown class", listener.getStanza() instanceof ChatListRetrieveResponceIQ);

                final ChatListRetrieveResponceIQ stanza = (ChatListRetrieveResponceIQ) listener.getStanza();

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

    @Test
    public void testUnsuccessfulResponce() throws NotConnectedException, InterruptedException {

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

        final ChatListRetrieveRequestIQ iq = new ChatListRetrieveRequestIQ("non_existing_user@maxlaptop");
        iq.setMax(30);

        connection.sendIqWithResponseCallback(iq, listener, listener, timeout);

        synchronized(listener) {
            listener.wait();

            if(listener.getException() != null) {
                // an error occurred while waiting for response
                System.out.println("\n***************************************************");
                System.out.println("\ttestUnsuccessfulResponce method\n".concat(listener.getException().getMessage()));
                System.out.println("***************************************************\n");

            } else {
                fail("'item-not-found' exception expected");
            }
        }

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
