package com.ua.max.oliynick.flicker.model;

import com.ua.max.oliynick.flicker.error.ChatException;
import com.ua.max.oliynick.flicker.interfaces.IChatModel;
import com.ua.max.oliynick.flicker.singleton.MainApp;
import com.ua.max.oliynick.flicker.util.ChatWrapper;
import com.ua.max.oliynick.flicker.util.MessageModel;
import com.ua.max.oliynick.flicker.util.StringUtils;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.ChatStateListener;
import org.jivesoftware.smackx.chatstates.ChatStateManager;
import org.jxmpp.util.XmppStringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import roboguice.inject.ContextSingleton;
import xmpp.extensions.com.ua.max.oliynick.interfaces.AbstractArchiveManager;
import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRequestIQ;
import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRetrieveRequestIQ;
import xmpp.extensions.com.ua.max.oliynick.service.ArchiveManager;
import xmpp.extensions.com.ua.max.oliynick.service.entity.ArchivedChat;
import xmpp.extensions.com.ua.max.oliynick.service.entity.ArchivedChatPage;
import xmpp.extensions.com.ua.max.oliynick.service.entity.ArchivedMessage;
import xmpp.extensions.com.ua.max.oliynick.service.entity.ArchivedMessagePage;

import static com.ua.max.oliynick.flicker.util.MessageModel.fromArchivedMessage;
import static com.ua.max.oliynick.flicker.util.MessageModel.fromXMPPMessage;

/**
 * Created by Максим on 23.02.2016.
 */
@ContextSingleton
public final class ChatModel extends IChatModel {

    private final class MessageLoader {

        private final AbstractArchiveManager archiveManager;
        private int index;

        MessageLoader() {
            archiveManager = ArchiveManager.getInstanceFor(MainApp.getConnection());
        }

        public synchronized Collection<MessageModel> retrieve() throws Exception {

            refreshIndex();

            final List<MessageModel> result = new ArrayList<>((int) 1.5 * getPerRequest() + 1);

            final ChatListRequestIQ retrIQ = new ChatListRequestIQ();
            retrIQ.setIndex(index);
            retrIQ.setMax(index < 0 ? getPerRequest() + index : getPerRequest());

            final ArchivedChatPage page = archiveManager.retreiveArchivedChats(retrIQ);
            index = page.getIndex() - getPerRequest();// 'step back'

            final ArchivedChat[] chats =
                    page.getChats().toArray(new ArchivedChat[page.getChats().size()]);

            for (int i = 0; i < chats.length; ++i) {

                final ChatListRetrieveRequestIQ messIQ =
                        new ChatListRetrieveRequestIQ(chats[i].getWith());
                messIQ.setStart(chats[i].getStart());

                final ArchivedMessagePage messPage =
                        archiveManager.retreiveArchivedMessages(messIQ);

                for (final ArchivedMessage archivedMessage : messPage.getMessages()) {
                    result.add(fromArchivedMessage(archivedMessage, chats[i].getWith()));
                }

            }

            Collections.sort(result);
            return Collections.unmodifiableCollection(result);
        }

        private void refreshIndex() throws Exception {
            // refreshes index
            final ChatListRequestIQ countIQ = new ChatListRequestIQ();
            countIQ.setWith(XmppStringUtils.parseBareJid(chat.getParticipant()));
            countIQ.setMax(0);

            index = archiveManager.retreiveArchivedChats(countIQ).getCount() - getPerRequest();
        }

    }

    private final class MessageEventProcessor implements ChatStateListener {

        @Override
        public void stateChanged(Chat chat, ChatState state) {

            //TODO internationalize and(or) add appropriate switches
            switch (state) {
                case composing :
                    fireNotification(StringUtils.
                            parseBareName(chat.getParticipant()).concat(" is composing..."));
                    break;
                case active :
                    break;
                case inactive :
                    break;
                case paused :
                    break;
                case gone :
                    break;
            }
        }

        @Override
        public void processMessage(Chat chat, Message message) {
            fireNewMessage(fromXMPPMessage(message, System.currentTimeMillis()));
        }

    }

    private static int perRequest = 20;

    private Chat chat;

    private MessageLoader loader = null;

    public ChatModel() {
        init(null);
    }

    public ChatModel(final String jid) {
        init(jid);
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(final Chat chat) {
        this.chat = chat;
    }

    public void setChat(final String jid) {
        final ChatWrapper wrappedChat = MainApp.getChatCreationManager().getChat(jid);
        Chat chat;

        if(wrappedChat == null) {
            chat = MainApp.getChatCreationManager().createChat(jid);
        } else {
            chat = wrappedChat.getChat();
        }

        this.chat = chat;
        this.chat.addMessageListener(new MessageEventProcessor());
    }

    public static int getPerRequest() {
        return perRequest;
    }

    public static void setPerRequest(int perRequest) {
        ChatModel.perRequest = perRequest;
    }

    @Override
    public Collection<MessageModel> loadMessages(int max) throws Exception {
        //TODO finish
        return loader.retrieve();
    }

    @Override
    public Collection<MessageModel> nextMessages(int max) throws Exception {
        //TODO finish
        return loader.retrieve();
    }

    @Override
    public void sendMessage(String message) throws ChatException {

        try {
            chat.sendMessage(message);
        } catch (SmackException.NotConnectedException exc) {
            // TODO set proper message, add some info
            throw new ChatException(exc.getMessage());
        }

    }

    @Override
    public void sendComposingNotification() {
        try {
            ChatStateManager.getInstance(MainApp.getConnection()).
                    setCurrentState(ChatState.composing, chat);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    private void init(final String jid) {
        if(jid != null) setChat(jid);

        loader = new MessageLoader();
    }

}
