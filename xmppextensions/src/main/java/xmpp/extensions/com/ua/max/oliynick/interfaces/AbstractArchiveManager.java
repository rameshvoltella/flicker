package xmpp.extensions.com.ua.max.oliynick.interfaces;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRequestIQ;
import xmpp.extensions.com.ua.max.oliynick.iq.ChatListResponceIQ;
import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRetrieveRequestIQ;
import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRetrieveResponceIQ;
import xmpp.extensions.com.ua.max.oliynick.provider.ChatListResponceIQProvider;
import xmpp.extensions.com.ua.max.oliynick.provider.ChatListRetrieveIQProvider;
import xmpp.extensions.com.ua.max.oliynick.service.entity.ArchivedChatPage;
import xmpp.extensions.com.ua.max.oliynick.service.entity.ArchivedMessagePage;

/**
 * <p>
 * This class manages archived messages
 * on the Openfire server using XEP-0136
 * </p>
 * @author Max Oliynick
 * */
public abstract class AbstractArchiveManager {

    /**
     * <p>
     * Registered {@link IArchivedChatPageListener} listeners
     * </p>
     * */
    private List<IArchivedChatPageListener> archivedChatListeners;

    /**
     * <p>
     * Registered {@link IArchivedMessagePageListener} listeners
     * </p>
     * */
    private List<IArchivedMessagePageListener> archivedMessageListeners;

    protected AbstractArchiveManager() {
        archivedChatListeners = new ArrayList<>();
        archivedMessageListeners = new ArrayList<>();
        registerProviders();
    }

    /**
     * <p>
     * Registers given listener
     * </p>
     * */
    public void addArchivedChatPageListener(final IArchivedChatPageListener listener) {
        archivedChatListeners.add(listener);
    }

    /**
     * <p>
     * Unregisters given listener
     * </p>
     * */
    public void removeArchivedChatPageListener(final IArchivedChatPageListener listener) {
        archivedChatListeners.remove(listener);
    }

    /**
     * <p>
     * Returns unmodifiable collection of all registered listeners
     * </p>
     * */
    public Collection<IArchivedChatPageListener> getArchivedChatPageListeners() {
        return Collections.unmodifiableCollection(archivedChatListeners);
    }

    /**
     * Convenient method to fire all registered listeners
     * */
    protected synchronized void fireArchChatListeners(final ChatListResponceIQ iq) {
        synchronized (archivedChatListeners) {
            final ArchivedChatPage page = new ArchivedChatPage(iq);

            for(final IArchivedChatPageListener listener : archivedChatListeners) {
                listener.responceReceived(page);
            }

        }
    }

    /**
     * <p>
     * Registers given listener
     * </p>
     * */
    public void addArchivedMessagePageListener(final IArchivedMessagePageListener listener) {
        archivedMessageListeners.add(listener);
    }

    /**
     * <p>
     * Unregisters given listener
     * </p>
     * */
    public void removeArchivedMessagePageListener(final IArchivedMessagePageListener listener) {
        archivedMessageListeners.remove(listener);
    }

    /**
     * <p>
     * Returns unmodifiable collection of all registered listeners
     * </p>
     * */
    public Collection<IArchivedMessagePageListener> getArchivedMessagePageListeners() {
        return Collections.unmodifiableCollection(archivedMessageListeners);
    }

    /**
     * Convenient method to fire all registered listeners
     * */
    protected synchronized void fireArchMsgListeners(final ChatListRetrieveResponceIQ iq) {
        synchronized (archivedMessageListeners) {
            final ArchivedMessagePage page = new ArchivedMessagePage(iq);

            for(final IArchivedMessagePageListener listener : archivedMessageListeners) {
                listener.responceReceived(page);
            }

        }
    }

    /**
     * Registers necessary IQ providers
     * */
    protected void registerProviders() {

        ProviderManager.addIQProvider(ChatListResponceIQProvider.elementName,
                ChatListResponceIQProvider.namespace,
                new ChatListResponceIQProvider());

        ProviderManager.addIQProvider(ChatListRetrieveIQProvider.elementName,
                ChatListRetrieveIQProvider.namespace,
                new ChatListRetrieveIQProvider());
    }

    /**
     * <p>
     * Retrieves archived chats for a given request IQ.
     * An implementation of this method should be synchronous method
     * </p>
     * @param iq request IQ
     * @throws Exception
     * */
    public abstract ArchivedChatPage retreiveArchivedChats(final ChatListRequestIQ iq) throws Exception;

    /**
     * <p>
     * Retrieves all archived chats.
     * An implementation of this method should be synchronous method
     * </p>
     * @throws Exception
     * */
    public abstract ArchivedChatPage retreiveArchivedChats() throws Exception;

    /**
     * <p>
     * Retrieves archived messages for a given request IQ.
     * An implementation of this method should be synchronous method
     * </p>
     * @param iq request IQ
     * @throws Exception
     * */
    public abstract ArchivedMessagePage retreiveArchivedMessages(final ChatListRetrieveRequestIQ iq) throws Exception;

    /**
     * <p>
     * Sends given IQ packet to a server
     * </p>
     * @param iq request stanza
     * @throws Exception
     * */
    public abstract void sendIQ(final IQ iq) throws Exception;

}
