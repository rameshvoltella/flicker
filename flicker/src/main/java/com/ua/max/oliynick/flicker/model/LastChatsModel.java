package com.ua.max.oliynick.flicker.model;

import com.ua.max.oliynick.flicker.interfaces.ILastChatsModel;
import com.ua.max.oliynick.flicker.singleton.MainApp;
import com.ua.max.oliynick.flicker.util.LastChatModel;
import com.ua.max.oliynick.flicker.util.StringUtils;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import java.util.Arrays;
import java.util.Collection;

import roboguice.inject.ContextSingleton;

/**
 * <p>
 * This model is responsible for
 * last conversations managing
 * </p>
 * <p>Any controller that wants to receive events
 * should be subscribed to them</p>
 * Created by Максим on 20.02.2016.
 */
@ContextSingleton
public final class LastChatsModel extends ILastChatsModel {

    private ChatManager chatManager = null;

    private ChatManagerListener listener = null;

    /**Simple custom implementation of chat manager listener*/
    private final class ChatCreationListener implements ChatManagerListener {

        @Override
        public void chatCreated(Chat chat, boolean createdLocally) {

            final LastChatModel model =
                    new LastChatModel(StringUtils.parseBareJid(chat.getParticipant()));

            model.setPresenceType(Presence.Type.available);
            model.setLogin(StringUtils.parseBareName(chat.getParticipant()));
            model.setChat(chat);

            if(doAddLastChatModel(model)) {

                fireChatCreated(model);

                // fires each time this chat receives a new message
                chat.addMessageListener(new ChatMessageListener() {
                    @Override
                    public void processMessage(Chat chat, Message message) {
                        model.setPresenceType(Presence.Type.available);
                        model.setMessage(message.getBody());
                        model.setLogin(StringUtils.parseBareName(message.getFrom()));
                        model.setChat(chat);
                        fireChatUpdated(model);
                    }
                });
            }
        }
    }

    public LastChatsModel() {
        super();
        chatManager = ChatManager.getInstanceFor(MainApp.getConnection());
        listener = new ChatCreationListener();
        chatManager.addChatListener(listener);
    }

    @Override
    public Collection<LastChatModel> getLastConversations() {
        //TODO finish
        LastChatModel m = new LastChatModel("mary@maxlaptop");
        m.setLogin("mary");
        m.setPresenceType(Presence.Type.available);
        m.setMessage("Hello Max, A couple of years ago, while working on my master's degree, I was" +
                " a teaching assistant for a first-year Java programming course. For one of the labs," +
                " I was given the lab material which was to be on the topic of stacks and using linked " +
                "lists to implement them. I was also given the reference \"solution\". This is the solution" +
                " Java file I was given, almost verbatim (I removed the comments to save some space");
        //return lastChats;
        return Arrays.asList(m);
    }

    @Override
    protected void doDestroyChat(final LastChatModel model) {
        model.getChat().close();
    }

}
