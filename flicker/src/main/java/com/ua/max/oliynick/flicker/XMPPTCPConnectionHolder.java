package com.ua.max.oliynick.flicker;

import android.util.Log;

import com.ua.max.oliynick.flicker.interfaces.ISettings;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

/**
 * Created by Максим on 21.01.2016.
 */
public class XMPPTCPConnectionHolder extends XMPPTCPConnection {

    private static XMPPTCPConnectionHolder instance = null;

    private XMPPTCPConnectionHolder(final XMPPTCPConnectionConfiguration config) {
        super(config);
    }

    public static synchronized void initInstance(final ISettings settings) throws IOException, XMPPException, SmackException {
        if(instance == null) {
            instance = new XMPPTCPConnectionHolder(buildConfig(settings));
            instance.connect();
        }
    }

    public static XMPPTCPConnectionHolder getInstance() {
        return instance;
    }

    private static XMPPTCPConnectionConfiguration buildConfig(final ISettings settings) {
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setHost(settings.getHost()).
                setServiceName(settings.getServiceName()).
                setPort(settings.getPort()).
                setCompressionEnabled(settings.isCompressionEnabled()).
                setSendPresence(settings.isSendPresence()).
                setConnectTimeout(settings.getConnectionTimeout());

        return builder.build();
    }

    @Override
    protected void afterSuccessfulLogin(boolean resumed) throws SmackException.NotConnectedException {
        super.afterSuccessfulLogin(resumed);
        Log.d("success login", "successfull login " + resumed);
    }
}
