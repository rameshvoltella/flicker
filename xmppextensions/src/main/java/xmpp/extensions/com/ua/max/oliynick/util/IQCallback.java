package xmpp.extensions.com.ua.max.oliynick.util;

import org.jivesoftware.smack.ExceptionCallback;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Stanza;

/**
 * Custom listener interface
 * */
public interface IQCallback extends StanzaListener, ExceptionCallback {

    /**
     * Returns last stanza which
     * was received by {@link StanzaListener}
     * */
    public Stanza getStanza();

    /**
     * Returns last exception which
     * was received by {@link ExceptionCallback}
     * */
    public Exception getException();

}
