package xmpp.extensions.com.ua.max.oliynick.util;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Stanza;

public class DefaultIQCallback implements IQCallback {

    // Response stanza
    private Stanza stanza = null;

    // Exception in case of runtime exception
    private Exception exception = null;

    @Override
    public Stanza getStanza() {
        return stanza;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    @Override
    public synchronized void processPacket(final Stanza stanza)
            throws NotConnectedException {
        // response class should be AvailableChatsIQ
        this.stanza = stanza;
        notify();
    }

    @Override
    public synchronized void processException(final Exception exception) {
        this.exception = exception;
        notify();
    }

}
