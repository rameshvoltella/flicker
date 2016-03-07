package com.ua.max.oliynick.flicker.model;

import com.ua.max.oliynick.flicker.interfaces.ILastChatsModel;
import com.ua.max.oliynick.flicker.singleton.MainApp;
import com.ua.max.oliynick.flicker.util.ChatWrapper;
import com.ua.max.oliynick.flicker.util.GenericObserver;
import com.ua.max.oliynick.flicker.util.LastChatModel;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jxmpp.util.XmppStringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import roboguice.inject.ContextSingleton;
import xmpp.extensions.com.ua.max.oliynick.interfaces.AbstractArchiveManager;
import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRequestIQ;
import xmpp.extensions.com.ua.max.oliynick.service.ArchiveManager;
import xmpp.extensions.com.ua.max.oliynick.service.entity.ArchivedChat;
import xmpp.extensions.com.ua.max.oliynick.service.entity.ArchivedChatPage;

/**
 * <p>
 * This model is responsible for
 * last conversations managing
 * </p>
 * <p>Any controller that wants to receive events
 * should be subscribed to them
 * </p>
 * Created by Максим on 20.02.2016.
 */
@ContextSingleton
public final class LastChatsModel extends ILastChatsModel {

    /**
     * <p>
     *     Last chats loader
     * </p>
     * */
    private final class Loader {

        private final AbstractArchiveManager archiveManager;
        private final Map<String, LastChatModel> chatMap;
        private int index = 0;

        private int resultsPerPage = 15;

        Loader() {
            archiveManager = ArchiveManager.getInstanceFor(MainApp.getConnection());
            chatMap = new HashMap<>(25);
            // retrieves entries count from server
            ChatListRequestIQ countIQ = new ChatListRequestIQ();
            countIQ.setMax(0);
            try {
                index = archiveManager.retreiveArchivedChats(countIQ).getCount() - getResultsPerPage();
            } catch (Exception e) {
                e.printStackTrace();
                index = 0;
            }
        }

        public int getResultsPerPage() {
            return resultsPerPage;
        }

        public void setResultsPerPage(int resultsPerPage) {
            this.resultsPerPage = resultsPerPage;
        }

        public int getIndex() {
            return index;
        }

        /**
         * Cleans cache and tries to recalculate last index
         * */
        public boolean clean() {
            chatMap.clear();
            // retrieves entries count from server
            ChatListRequestIQ countIQ = new ChatListRequestIQ();
            countIQ.setMax(0);
            try {
                index = archiveManager.retreiveArchivedChats(countIQ).getCount() - getResultsPerPage();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                index = 0;
            }

            return false;
        }

        public Collection<LastChatModel> getLoaded() {
            return Collections.unmodifiableCollection(chatMap.values());
        }

        public boolean hasNext() {
            return index + getResultsPerPage() > 0;
        }

        public Collection<LastChatModel> next() throws Exception {
            return doLoadPage();
        }

        private Collection<LastChatModel> doLoadPage() throws Exception {

            if (!hasNext()) return null;

               final Set<LastChatModel> result = new HashSet<>();

                /*
                Dirty hack to make it possible to iterate over
                archived chats retrieved from server (XEP-0136
                extension for Openfire server contains many bugs)
                */
                final ChatListRequestIQ iq = new ChatListRequestIQ();
                iq.setIndex(index);
                // small optimization
                iq.setMax(index < 0 ? getResultsPerPage() + index : getResultsPerPage());

                final ArchivedChatPage page = archiveManager.retreiveArchivedChats(iq);
                index = page.getIndex() - getResultsPerPage();// 'step back'

                final ArchivedChat[] chats =
                        page.getChats().toArray(new ArchivedChat[page.getChats().size()]);
                int i = chats.length;

                /*
                * Reverse iteration of ordered server response
                * */
                while (i-- != 0) {

                    final String jid = chats[i].getWith();

                    if (!chatMap.containsKey(jid)) {

                        final LastChatModel model = new LastChatModel(jid);
                        model.setJid(jid);
                        model.setLogin(XmppStringUtils.parseLocalpart(jid));

                        chatMap.put(jid, model);
                        result.add(model);
                    }
                }

                return result;
        }

    }

    private final class ChatUpdateProcessor extends GenericObserver<ChatWrapper> {

        @Override
        public void onValueChanged(
                final Observable observable,
                final ChatWrapper oldValue,
                final ChatWrapper newValue) {

            newValue.getChat().addMessageListener(new ChatMessageListener() {

                @Override
                public void processMessage(final Chat chat, final Message message) {

                    final String id = XmppStringUtils.parseBareJid(chat.getParticipant());
                    LastChatModel model;

                    if (containsModel(id)) {// model was already registered
                        model = getModel(id);

                    } else {// register new model

                        model = new LastChatModel(id);
                        model.setLogin(XmppStringUtils.parseLocalpart(chat.getParticipant()));
                        model.setJid(XmppStringUtils.parseBareJid(chat.getParticipant()));

                        doAddLastChatModel(model);
                    }

                    model.setPresenceType(Presence.Type.available);
                    model.setMessage(message.getBody());

                    fireChatUpdated(model);
                }
            });
        }
    }

    private Loader loader = null;

    public LastChatsModel() {
        super();
        MainApp.getChatCreationManager().addChatCreationListener(new ChatUpdateProcessor());
        loader = new Loader();
    }

    @Override
    public Collection<LastChatModel> retrieveNextLastConv() throws Exception {
        return Collections.unmodifiableCollection(loader.next());
    }

    @Override
    public Collection<LastChatModel> loadNextLastConv() throws Exception {
        final Collection<LastChatModel> tmp = loader.next();

        for (final LastChatModel lastChatModel : tmp) {
            doAddLastChatModel(lastChatModel);
        }

        return Collections.unmodifiableCollection(tmp);
    }

    @Override
    public boolean hasNextLastConv() {
        return loader.hasNext();
    }

    @Override
    protected void doSynchronize() throws Exception {

        if(loader.clean()) {
            // inform that synchronization wasn't successful
        }

    }

}
