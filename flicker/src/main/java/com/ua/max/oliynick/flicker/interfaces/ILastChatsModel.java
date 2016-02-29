package com.ua.max.oliynick.flicker.interfaces;

import com.ua.max.oliynick.flicker.util.GenericObservable;
import com.ua.max.oliynick.flicker.util.GenericObserver;
import com.ua.max.oliynick.flicker.util.LastChatModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Максим on 20.02.2016.
 */
public abstract class ILastChatsModel {

    /**
     * <p>Holds all last conversations.</p>
     * */
    private Map<String, LastChatModel> lastConversations = null;

    private GenericObservable<LastChatModel> chatUpdateObservable = null;

    private GenericObservable<LastChatModel> chatDestroyObservable = null;

    public ILastChatsModel() {
        lastConversations = new HashMap<>();
        chatUpdateObservable = new GenericObservable<>();
        chatDestroyObservable = new GenericObservable<>();
    }

    public Collection<LastChatModel> getLastConversations() {
        return lastConversations.values();
    }

    /**
     * <p>
     *     Destroys given chat model. Note that in this case
     *     this model will be removed from last chats
     * </p>
     * */
    public void destroyChat(final LastChatModel model) {

        if(lastConversations.remove(model) != null) {
            doDestroyChat(model);
            fireChatDestroyed(model);
        }
    }

    public void addChatUpdateObserver(final GenericObserver<LastChatModel>  observer) {
        chatUpdateObservable.addObserver(observer);
    }

    public void removeChatUpdateObserver(final GenericObserver<LastChatModel> observer) {
        chatUpdateObservable.deleteObserver(observer);
    }

    public void addChatDestroyObserver(final GenericObserver<LastChatModel>  observer) {
        chatDestroyObservable.addObserver(observer);
    }

    public void removeChatDestroyObserver(final GenericObserver<LastChatModel> observer) {
        chatDestroyObservable.deleteObserver(observer);
    }

    protected void fireChatUpdated(final LastChatModel item) {
        chatUpdateObservable.setValue(item);
    }

    protected boolean containsModel(final String id) {
        return lastConversations.containsKey(id);
    }

    protected LastChatModel getModel(final String id) {
        return lastConversations.get(id);
    }

    protected void doAddLastChatModel(final LastChatModel model) {
        lastConversations.put(model.getId(), model);
    }

    protected void fireChatDestroyed(final LastChatModel item) {
        chatDestroyObservable.setValue(item);
    }

    /**
     * <p>Fires each time subclass calls {@code destroyChat} method.
     * This method should be implemented by subclasses in order
     * to make their own chat destroying logic</p>
     * */
    protected abstract void doDestroyChat(final LastChatModel model);

}
