package com.ua.max.oliynick.flicker.interfaces;

import com.ua.max.oliynick.flicker.util.GenericObservable;
import com.ua.max.oliynick.flicker.util.GenericObserver;
import com.ua.max.oliynick.flicker.util.LastChatModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Максим on 20.02.2016.
 */
public abstract class ILastChatsModel {

    /**
     * <p>Holds all last conversations.</p>
     * */
    private List<LastChatModel> lastConversations = null;

    private GenericObservable<LastChatModel> chatObservable = null;

    private GenericObservable<LastChatModel> chatUpdateObservable = null;

    private GenericObservable<LastChatModel> chatDestroyObservable = null;

    public ILastChatsModel() {
        lastConversations = new ArrayList<>();
        chatObservable = new GenericObservable<>();
        chatUpdateObservable = new GenericObservable<>();
        chatDestroyObservable = new GenericObservable<>();
    }

    public Collection<LastChatModel> getLastConversations() {
        return lastConversations;
    }

    /**
     * <p>
     *     Destroys given chat model. Note that in this case
     *     this model will be removed from last chats
     * </p>
     * */
    public void destroyChat(final LastChatModel model) {

        if(lastConversations.remove(model)) {
            doDestroyChat(model);
            fireChatDestroyed(model);
        }
    }

    public void addChatCreationObserver(final GenericObserver<LastChatModel>  observer) {
        chatObservable.addObserver(observer);
    }

    public void removeChatCreationObserver(final GenericObserver<LastChatModel> observer) {
        chatObservable.deleteObserver(observer);
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

    protected void fireChatCreated(final LastChatModel item) {
        chatObservable.setValue(item);
    }

    protected void fireChatUpdated(final LastChatModel item) {
        chatUpdateObservable.setValue(item);
    }

    protected boolean doAddLastChatModel(final LastChatModel model) {
        return lastConversations.add(model);
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
