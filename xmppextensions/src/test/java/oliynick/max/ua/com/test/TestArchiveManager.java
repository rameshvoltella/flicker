package oliynick.max.ua.com.test;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.joda.time.DateTime;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import xmpp.extensions.com.ua.max.oliynick.interfaces.AbstractArchiveManager;
import xmpp.extensions.com.ua.max.oliynick.interfaces.IArchivedChatPageListener;
import xmpp.extensions.com.ua.max.oliynick.interfaces.IArchivedMessagePageListener;
import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRequestIQ;
import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRetrieveRequestIQ;
import xmpp.extensions.com.ua.max.oliynick.service.ArchiveManager;
import xmpp.extensions.com.ua.max.oliynick.service.entity.ArchivedChat;
import xmpp.extensions.com.ua.max.oliynick.service.entity.ArchivedChatPage;
import xmpp.extensions.com.ua.max.oliynick.service.entity.ArchivedMessage;
import xmpp.extensions.com.ua.max.oliynick.service.entity.ArchivedMessagePage;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

public final class TestArchiveManager {

    // Test xmpp connection instance
    private static XMPPTCPConnection connection = null;

    // Test AbstractArchiveManager instance
    private static AbstractArchiveManager manager = null;

    @BeforeClass
    public static void prepareConnection() throws XMPPException, SmackException, IOException {
        connection = new XMPPTCPConnection(TestConfig.buildConfig());
        connection.connect();

        manager = ArchiveManager.getInstanceFor(connection);

        manager.addArchivedChatPageListener(new IArchivedChatPageListener() {

            @Override
            public void responceReceived(ArchivedChatPage responce) {
                System.out.println("\nArhived chat listener fired ".concat(responce.toString()));
            }
        });

        manager.addArchivedMessagePageListener(new IArchivedMessagePageListener() {

            @Override
            public void responceReceived(ArchivedMessagePage responce) {
                System.out.println("\nArhived message listener fired ".concat(responce.toString()));
            }
        });

        connection.login("maxxx", "qwerty");
    }

    /**
     * <p>
     * Tries to retrieve all archived chats
     * </p>
     * */
    @Test
    public void testRetrieveArchivedChats() throws Exception {
        System.out.println("\n******************************");
        System.out.println("testRetrieveArchivedChats");
        System.out.println(manager.retreiveArchivedChats());
        System.out.println("******************************\n");
    }

    /**
     * <p>
     * Tries to retrieve archived chats with given param
     * </p>
     * */
    @Test
    public void testRetrieveArchivedChatsParam() throws Exception {

        int max = 10;
        final ChatListRequestIQ iq = new ChatListRequestIQ("mary@maxlaptop", max);

        System.out.println("\n******************************");
        System.out.println("testRetrieveArchivedChatsParam");
        final ArchivedChatPage page = manager.retreiveArchivedChats(iq);
        System.out.println(page);
        System.out.println("******************************\n");

        assertTrue("Incorect page size", page.getChats().size() <= max);
        assertTrue("Incorect page size", page.getIndex() == 0);
    }

    /**
     * <p>
     * Tries to retrieve archived chats with given param
     * </p>
     * */
    @Test
    public void testRetrieveArchivedMessages() throws Exception {

        final String with = "mary@maxlaptop";
        final int max = 10;
        final ChatListRequestIQ chatIq = new ChatListRequestIQ(with, max);

        System.out.println("\n******************************");
        System.out.println("testRetrieveArchivedChatsParam");
        final ArchivedChatPage chatPage = manager.retreiveArchivedChats(chatIq);
        System.out.println("Retrieved chats: ".concat(chatPage.toString()));

        assumeTrue("Not enough data to test", chatPage.getCount() >= 1);

        final ChatListRetrieveRequestIQ messIq = new ChatListRetrieveRequestIQ(with);
        final DateTime start = chatPage.getChats().toArray(new ArchivedChat[1])[0].getStart();
        messIq.setStart(start);

        final ArchivedMessagePage messPage = manager.retreiveArchivedMessages(messIq);

        int secs = 0;

        for(final ArchivedMessage message : messPage.getMessages()) {
            System.out.println("Message: ".concat(message.toString()));

            // messages should be sorted by server
            assertTrue("Unordered responce, XER-0136 violation", message.getSecondsCreation() >= secs);
            secs = message.getSecondsCreation();
        }

        System.out.println("******************************\n");

        assertTrue("Incorect page size", chatPage.getChats().size() <= max);
        assertTrue("Incorect page size", chatPage.getIndex() == 0);
        assumeTrue(messPage.getStartDate().equals(start));
    }

    /**
     * <p>
     * Tests whether manager throws NullPointerException in case of
     * null argument
     * </p>
     * */
    @Test (expected = NullPointerException.class)
    public void testRetrieveArchivedChatsWithNull() throws Exception {
        manager.retreiveArchivedChats(null);
    }

    /**
     * <p>
     * Tests whether manager throws NullPointerException in case of
     * null argument
     * </p>
     * */
    @Test (expected = NullPointerException.class)
    public void testRetrieveArchivedMessagesWithNull() throws Exception {
        manager.retreiveArchivedMessages(null);
    }

    @AfterClass
    public static void tearDown() {
        if(connection != null) {
            connection.disconnect();
        } else {
            fail("Connection wasn't set properly!");
        }
    }

}
