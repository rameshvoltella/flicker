package xmpp.extensions.com.ua.max.oliynick.interfaces;

import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRequestIQ;
import xmpp.extensions.com.ua.max.oliynick.service.entity.ArchivedChatPage;

/**
 * <p>
 * Defines server response listener
 * on {@link ChatListRequestIQ}
 * </p>
 * @author Max Oliynick
 * */
public interface IArchivedChatPageListener {

    /**
     * <p>
     * This method fires every time {@link AbstractArchiveManager} retrieves
     * response on {@link ChatListRequestIQ}
     * </p>
     * @param responce server response
     * */
    void responceReceived(final ArchivedChatPage responce);

}
