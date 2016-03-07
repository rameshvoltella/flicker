package com.ua.max.oliynick.flicker.singleton;

import android.util.Log;

import com.ua.max.oliynick.flicker.util.ChatWrapper;
import com.ua.max.oliynick.flicker.util.GenericObservable;
import com.ua.max.oliynick.flicker.util.GenericObserver;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jxmpp.util.XmppStringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>This class is responsible for
 * listening of chat creation.
 * If xmmpp chat is
 * created, than all attached
 * observers will be notified</p>
 * Created by Максим on 19.02.2016.
 */
public final class ChatCreationManager {

    /**Observable property*/
    private GenericObservable<ChatWrapper> chatCreationProp = null;
    /**Chat creation listener*/
    private ChatManagerListener listener = null;
    /**Holds all chats*/
    private Map<String, ChatWrapper> chatMap = null;

    ChatCreationManager() {

        chatMap = new HashMap<>();
        chatCreationProp = new GenericObservable<>();

        listener = new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean createdLocally) {
                Log.d("SMACK", "chat created ".concat(chat.toString()));
                doPut(chat, createdLocally);
            }
        };

    }

    /**
     * <p>Attaches listener to a given chat manager</P>
     * @param chatManager given xmpp chat manager
     * @see ChatManager
     * */
    void attach(ChatManager chatManager) {
        chatManager.addChatListener(listener);
    }

    /**
     * <p>Detaches listener to a given chat manager</p>
     * @param chatManager given xmpp chat manager
     * @see ChatManager
     * */
    void detach(ChatManager chatManager) {
        chatManager.removeChatListener(listener);
    }

    /**
     * <p>Adds observer to chat creation property</p>
     * @param observer observer to add
     * */
    public void addChatCreationListener(GenericObserver<ChatWrapper> observer) {
        chatCreationProp.addObserver(observer);
    }

    /**
     * <p>Removes observer from chat creation property</p>
     * @param observer observer to remove
     * */
    public void removeChatCreationListener(GenericObserver<ChatWrapper> observer) {
        chatCreationProp.deleteObserver(observer);
    }

    public Chat createChat(final String jid) {
        return ChatManager.getInstanceFor(MainApp.getConnection()).createChat(jid);
    }

    /**<p>
     * Returns previously created chat wrapper for a given jid.
     * If chat wasn't created, than it'll return null
     * @param jid jabber user id
     * </p>*/
    public ChatWrapper getChat(final String jid) {
        return chatMap.get(XmppStringUtils.parseBareJid(jid));
    }

    private void doPut(Chat chat, boolean createdLocally) {
        final ChatWrapper chatWrapper = new ChatWrapper(createdLocally, chat);

        chatCreationProp.setValue(chatWrapper);
        chatMap.put(XmppStringUtils.parseBareJid(chat.getParticipant()), chatWrapper);
    }

}
