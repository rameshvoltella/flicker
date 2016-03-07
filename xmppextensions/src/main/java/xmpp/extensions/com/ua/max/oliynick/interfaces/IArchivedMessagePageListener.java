package xmpp.extensions.com.ua.max.oliynick.interfaces;

import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRetrieveRequestIQ;
import xmpp.extensions.com.ua.max.oliynick.service.entity.ArchivedMessagePage;

/**
 * <p>
 * Defines server response listener
 * on {@link ChatListRetrieveRequestIQ}
 * </p>
 * @author Max Oliynick
 * */
public interface IArchivedMessagePageListener {

    /**
     * <p>
     * This method fires every time {@link AbstractArchiveManager} retrieves
     * response on {@link ChatListRetrieveRequestIQ}
     * </p>
     * @param responce server response
     * */
    void responceReceived(final ArchivedMessagePage responce);

}
