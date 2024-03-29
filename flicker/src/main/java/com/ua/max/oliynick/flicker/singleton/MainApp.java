package com.ua.max.oliynick.flicker.singleton;

import android.app.Application;

import com.ua.max.oliynick.flicker.interfaces.IInitializible;
import com.ua.max.oliynick.flicker.interfaces.ISettings;
import com.ua.max.oliynick.flicker.util.ConnectionInitializer;
import com.ua.max.oliynick.flicker.util.GenericObserver;

import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.util.Observable;

/**
 * <p>This class initializes application at the beginning.
 * This class should be used to access global services,
 * data and etc.</p>
 * Created by Максим on 21.02.2016.
 */
public class MainApp extends Application {

    /**Application instance*/
    private static MainApp instance = null;

    /**Global settings*/
    private static ISettings settings = null;

    /**XMPP connection*/
    private static XMPPTCPConnection connection = null;

    /**Global connection listener*/
    private static ConnectionListener connectionListener = null;

    private static ChatCreationManager chatCreationListener = null;

    /**Returns reference to global {@linkplain MainApp} object*/
    public MainApp getInstance(){
        return instance;
    }

    /**Returns reference to global {@linkplain ISettings} object*/
    public static XMPPTCPConnection getConnection() {
        return connection;
    }

    /**Returns reference to global {@linkplain ISettings} object*/
    public static ISettings getSettings() {
        return settings;
    }

    /**Returns reference to global {@linkplain ConnectionListener} object*/
    public static ConnectionListener getConnectionListener() {
        return connectionListener;
    }

    /**Returns reference to global {@linkplain ChatCreationManager} object*/
    public static ChatCreationManager getChatCreationManager() {
        return chatCreationListener;
    }

    public MainApp() {
        super();
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Initializing settings with app context
        settings = new Settings(getApplicationContext());
        connection = new XMPPTCPConnection(buildConnectionConfig(settings));
        connectionListener = new ConnectionListener(getApplicationContext());

        // appending chat creation listener to the xmpp connection
        connectionListener.addAuthPropertyListener(new GenericObserver<Boolean>() {
            @Override
            public void onValueChanged(Observable observable, Boolean oldValue, Boolean newValue) {

                if(chatCreationListener == null) {
                    chatCreationListener = new ChatCreationManager();
                }

                if (newValue) {
                    chatCreationListener.attach(ChatManager.getInstanceFor(connection));
                }
            }
        });

        /*
        Initializes xmpp connection based on user settings.
        Note that this process is invoked in a separated thread
        * */
        final IInitializible connInitializer = new ConnectionInitializer();
        connInitializer.initialize(getApplicationContext());
    }

    /**
     * Builds xmpp connection configuration using given settings
     * */
    private XMPPTCPConnectionConfiguration buildConnectionConfig(final ISettings settings) {
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setHost(settings.getHost()).
                setServiceName(settings.getServiceName()).
                setPort(settings.getPort()).
                setCompressionEnabled(settings.isCompressionEnabled()).
                setSendPresence(settings.isSendPresence()).
                setConnectTimeout(settings.getConnectionTimeout());

        return builder.setDebuggerEnabled(true).build();
    }

}
