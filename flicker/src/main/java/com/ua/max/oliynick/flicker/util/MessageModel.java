package com.ua.max.oliynick.flicker.util;

import com.ua.max.oliynick.flicker.singleton.MainApp;

import org.jivesoftware.smack.packet.Message;
import org.joda.time.DateTime;
import org.jxmpp.util.XmppStringUtils;

import xmpp.extensions.com.ua.max.oliynick.iq.ChatListRetrieveResponceIQ;
import xmpp.extensions.com.ua.max.oliynick.service.entity.ArchivedMessage;

/**
 * Created by Максим on 07.03.2016.
 */
public final class MessageModel implements Comparable<MessageModel> {

    private final ChatListRetrieveResponceIQ.Direction direction;

    private final String body;

    private final DateTime timeStamp;

    private final String title;

    public MessageModel(ChatListRetrieveResponceIQ.Direction direction, String body,
                        String title, DateTime timeStamp) {

        if(direction == null)
            throw new NullPointerException("direction == null");

        if(body == null)
            throw new NullPointerException("body == null");

        if(title == null)
            throw new NullPointerException("title == null");

        if(timeStamp == null)
            throw new NullPointerException("timeStamp == null");

        this.direction = direction;
        this.body = body;
        this.title = title;
        this.timeStamp = timeStamp;
    }

    public static MessageModel fromXMPPMessage(final Message message, final long millis) {
        return new MessageModel(
                ChatListRetrieveResponceIQ.Direction.to,
                message.getBody(),
                XmppStringUtils.escapeLocalpart(message.getFrom()),
                new DateTime(millis)
        );
    }

    public static MessageModel fromArchivedMessage(final ArchivedMessage archMess, final String with) {
        return new MessageModel(
                archMess.getDirection(),
                archMess.getMessageBody(),
                XmppStringUtils.parseLocalpart( archMess.getDirection() == ChatListRetrieveResponceIQ.Direction.to ?
                        with : MainApp.getConnection().getUser() ),
                archMess.getFullSecs()
        );
    }

    public ChatListRetrieveResponceIQ.Direction getDirection() {
        return direction;
    }

    public String getBody() {
        return body;
    }

    public DateTime getTimeStamp() {
        return timeStamp;
    }

    public String getTitle() {
        return title;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageModel that = (MessageModel) o;

        if (direction != that.direction) return false;
        if (!body.equals(that.body)) return false;
        if (!timeStamp.equals(that.timeStamp)) return false;
        return title.equals(that.title);

    }

    @Override
    public int hashCode() {
        int result = direction.hashCode();
        result = 31 * result + body.hashCode();
        result = 31 * result + timeStamp.hashCode();
        result = 31 * result + title.hashCode();
        return result;
    }

    @Override
    public int compareTo(MessageModel another) {
        return getTimeStamp().compareTo(another.getTimeStamp());
    }
}
