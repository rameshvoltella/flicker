package com.ua.max.oliynick.flicker.util;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;

import java.util.List;

/**
 * Created by Максим on 20.02.2016.
 */
public final class ChatWrapper {
    private boolean createdLocally;
    private Chat chat;
    private List<Message> messages = null;

    public ChatWrapper(boolean createdLocally, Chat chat) {
        this.createdLocally = createdLocally;
        this.chat = chat;
    }

    public boolean isCreatedLocally() {
        return createdLocally;
    }

    public void setCreatedLocally(boolean createdLocally) {
        this.createdLocally = createdLocally;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
}
