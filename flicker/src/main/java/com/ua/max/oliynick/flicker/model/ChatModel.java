package com.ua.max.oliynick.flicker.model;

import com.ua.max.oliynick.flicker.error.ChatException;
import com.ua.max.oliynick.flicker.interfaces.IChatModel;
import com.ua.max.oliynick.flicker.singleton.MainApp;
import com.ua.max.oliynick.flicker.util.StringUtils;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.ChatStateListener;
import org.jivesoftware.smackx.chatstates.ChatStateManager;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by Максим on 23.02.2016.
 */
//@ContextSingleton
public final class ChatModel extends IChatModel {

    private Chat chat;

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
            fireNewMessage(message);
        }

    }

    public ChatModel(final Chat chat) {
        this.chat = chat;
        this.chat.addMessageListener(new MessageEventProcessor());
    }

    public Chat getChat() {
        return chat;
    }

    public void addChat(Chat chat) {
        chat.addMessageListener(new MessageEventProcessor());
        this.chat = chat;
    }

    @Override
    public Collection<Message> loadMessages(int max) {
        //TODO finish
        return Collections.emptyList();
    }

    @Override
    public Collection<Message> nextMessages(int max) {
        //TODO finish
        return Collections.emptyList();
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
}
