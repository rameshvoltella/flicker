package com.ua.max.oliynick.flicker.util;

import org.jivesoftware.smack.packet.Presence;

/**
 * Created by Максим on 20.02.2016.
 */
public class LastChatModel {

    public enum UpdatedProperty {Presence, Nickname, Avatar, Message, None};

    private final String id;
    private String login;
    private String message;
    private Presence.Type presenceType;

    public LastChatModel(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Presence.Type getPresenceType() {
        return presenceType;
    }

    public void setPresenceType(Presence.Type presenceType) {
        this.presenceType = presenceType;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LastChatModel that = (LastChatModel) o;

        if (!id.equals(that.id)) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        return presenceType == that.presenceType;

    }

    @Override
    public int hashCode() {
        return 33 * id.hashCode();
    }
}
