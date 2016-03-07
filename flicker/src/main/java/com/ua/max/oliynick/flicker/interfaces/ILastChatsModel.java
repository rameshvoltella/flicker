package com.ua.max.oliynick.flicker.interfaces;

import com.ua.max.oliynick.flicker.util.GenericObservable;
import com.ua.max.oliynick.flicker.util.GenericObserver;
import com.ua.max.oliynick.flicker.util.LastChatModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     This abstract class defines responsibilities
 *     of model which will supply controllers with
 *     info about last conversations
 * </p>
 * Created by Максим on 20.02.2016.
 */
public abstract class ILastChatsModel {

    /**
     * <p>
     *     Holds all last conversations.
     * </p>
     * */
    private final Map<String, LastChatModel> lastConversations;

    /**
     * <p>
     *     Observable for the most last chat. Holds null
     *     at the beginning
     * </p>
     * */
    private final GenericObservable<LastChatModel> chatUpdateObservable;

    public ILastChatsModel() {
        lastConversations = new HashMap<>();
        chatUpdateObservable = new GenericObservable<>();
    }

    /**
     * <p>
     *     Returns UNSORTED and UNMODIFIABLE collection of
     *     registered chat models
     * </p>
     * */
    public Collection<LastChatModel> getLastConversations() {
        return Collections.unmodifiableCollection(lastConversations.values());
    }

    /**
     * <p>
     *     Returns SORTED by a given comparator but UNMODIFIABLE collection of
     *     registered chat models
     * </p>
     * @param comparator comparator to use
     * */
    public Collection<LastChatModel> getLastConversations(Comparator<LastChatModel> comparator) {
        List<LastChatModel> sortedList = new ArrayList<LastChatModel>(lastConversations.values());
        Collections.sort(sortedList, comparator);
        return sortedList;
    }

    /**
     * <p>
     *     Tries to synchronize data with server.
     *     Will throw an exception in case of failure.
     *     Synchronize logic has to be implemented by
     *     subclasses
     * </p>
     * */
    public void synchronize() throws Exception {
        lastConversations.clear();
        doSynchronize();
    }

    /**
     * <p>
     *     Adds observer for chat update observable
     * </p>
     * @param observer observer to add
     * */
    public void addChatUpdateObserver(final GenericObserver<LastChatModel>  observer) {
        chatUpdateObservable.addObserver(observer);
    }

    /**
     * <p>
     *     Removes observer for chat update observable
     * </p>
     * @param observer observer to remove
     * */
    public void removeChatUpdateObserver(final GenericObserver<LastChatModel> observer) {
        chatUpdateObservable.deleteObserver(observer);
    }

    /**
     * <p>
     *     Fires chat update observable
     * </p>
     * @param item new value
     * */
    protected void fireChatUpdated(final LastChatModel item) {
        if(item == null)
            throw new NullPointerException("item == null");

        chatUpdateObservable.setValue(item);
    }

    /**
     * <p>
     *     Returns true if model with specified id was registered and
     *     false in another case
     * </p>
     * @param id id to search
     * */
    protected boolean containsModel(final String id) {
        return id != null && lastConversations.containsKey(id);
    }

    /**
     * <p>
     *     Returns model instance with specified id if she was registered and
     *     null in another case
     * </p>
     * @param id id to search
     * */
    protected LastChatModel getModel(final String id) {
        if(id == null) return null;
        return lastConversations.get(id);
    }

    /**
     * <p>
     *     Tries to register model instance
     * </p>
     * @param item item to register
     * */
    protected void doAddLastChatModel(final LastChatModel item) {
        if(item == null)
            throw new NullPointerException("item == null");

        lastConversations.put(item.getId(), item);
    }

    /**
     * <p>
     *     Returns collection of older conversations but doesn't register them
     * </p>
     * */
    public abstract Collection<LastChatModel> retrieveNextLastConv() throws Exception;

    /**
     * <p>
     *     Returns collection of older conversations and do register them
     * </p>
     * */
    public abstract Collection<LastChatModel> loadNextLastConv() throws Exception;

    /**
     * <p>
     *     Checks whether server has older conversations
     * </p>
     * */
    public abstract boolean hasNextLastConv();

    /**
     * <p>
     *     This method should be implemented by subclasses.
     *     Contains synchronization-dependent logic
     * </p>
     * */
    protected abstract void doSynchronize() throws Exception;

}
