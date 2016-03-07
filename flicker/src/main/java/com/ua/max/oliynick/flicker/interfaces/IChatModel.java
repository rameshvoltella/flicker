package com.ua.max.oliynick.flicker.interfaces;

import com.ua.max.oliynick.flicker.error.ChatException;
import com.ua.max.oliynick.flicker.util.GenericObservable;
import com.ua.max.oliynick.flicker.util.GenericObserver;
import com.ua.max.oliynick.flicker.util.MessageModel;

import java.util.Collection;

/**
 * Created by Максим on 23.02.2016.
 */
public abstract class IChatModel {

    private GenericObservable<MessageModel> messageObservable = null;
    private GenericObservable<String> presenceObservable = null;
    private GenericObservable<String> infoObservable = null;

    public IChatModel() {
        messageObservable = new GenericObservable<>();
        presenceObservable = new GenericObservable<>();
        infoObservable = new GenericObservable<>();
    }

    public void addMessageObserver(final GenericObserver<MessageModel> observer) {
        messageObservable.addObserver(observer);
    }

    public void removeMessageObserver(final GenericObserver<MessageModel> observer) {
        messageObservable.deleteObserver(observer);
    }

    public void addPresenceObserver(final GenericObserver<String> presenceObserver) {
        presenceObservable.addObserver(presenceObserver);
    }

    public void removePresenceObserver(final GenericObserver<String> presenceObserver) {
        presenceObservable.deleteObserver(presenceObserver);
    }

    public void addInfoObserver(final GenericObserver<String> presenceObserver) {
        infoObservable.addObserver(presenceObserver);
    }

    public void removeInfoObserver(final GenericObserver<String> presenceObserver) {
        infoObservable.deleteObserver(presenceObserver);
    }

    public abstract Collection<MessageModel> loadMessages(int max) throws Exception;

    public abstract Collection<MessageModel> nextMessages(int max) throws Exception;

    public abstract void sendMessage(String message) throws ChatException;

    public abstract void sendComposingNotification();

    protected void fireNewMessage(final MessageModel message) {
        messageObservable.setValue(message);
    }

    protected void fireNotification(final String message) {
        infoObservable.setValue(message);
    }

    protected void fireNewPresence(final String presence) {
        presenceObservable.setValue(presence);
    }

}
