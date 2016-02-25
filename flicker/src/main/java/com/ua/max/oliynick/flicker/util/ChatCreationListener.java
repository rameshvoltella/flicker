package com.ua.max.oliynick.flicker.util;

import android.util.Log;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;

/**
 * <p>This class is responsible for
 * listening of chat creation.
 * If xmmpp chat is
 * created, than all attached
 * observers will be notified</p>
 * Created by Максим on 19.02.2016.
 */
public final class ChatCreationListener {

    private static ChatCreationListener instance = new ChatCreationListener();

    public static final class ChatWrapper {
        private boolean createdLocally;
        private Chat chat;

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

    /**Observable property*/
    private GenericObservable<ChatWrapper> chatCreationProp = null;
    /**Chat creation listener*/
    private ChatManagerListener listener = null;

    private ChatCreationListener() {

        chatCreationProp = new GenericObservable<>();

        listener = new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean createdLocally) {
                Log.d("smack", "chat created ".concat(chat.toString()));
                chatCreationProp.setValue(new ChatWrapper(createdLocally, chat));
            }
        };

    }

    /**
     * Returns instance of {@code ChatCreationListener} class
     * */
    public static ChatCreationListener getInstance() {
        return instance;
    }

    /**
     * <p>Attaches listener to a given chat manager</P>
     * @param chatManager given xmpp chat manager
     * @see ChatManager
     * */
    public void attach(ChatManager chatManager) {
        chatManager.addChatListener(listener);
    }

    /**
     * <p>Detaches listener to a given chat manager</p>
     * @param chatManager given xmpp chat manager
     * @see ChatManager
     * */
    public void detach(ChatManager chatManager) {
        chatManager.removeChatListener(listener);
    }

    /**
     * <p>Adds observer to chat creation property</p>
     * @param observer observer to add
     * */
    public void addListener(GenericObserver<ChatWrapper> observer) {
        chatCreationProp.addObserver(observer);
    }

    /**
     * <p>Removes observer from chat creation property</p>
     * @param observer observer to remove
     * */
    public void removeListener(GenericObserver<ChatWrapper> observer) {
        chatCreationProp.deleteObserver(observer);
    }

}
