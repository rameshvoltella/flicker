package com.ua.max.oliynick.flicker.singleton;

import android.content.Context;
import android.content.SharedPreferences;

import com.ua.max.oliynick.flicker.interfaces.ISettings;

import org.jivesoftware.smack.ReconnectionManager;

/**
 * Created by Максим on 21.01.2016.
 */
public final class Settings implements ISettings {

    private static final String LOCATION = "flickerAppSettings";

    public static final String DEFAULT_SERVICE_NAME = "maxlaptop";

    /*Default preferences keys*/
    private static final String SERVICE_NAME_KEY = "serviceName";
    private static final String HOST_KEY = "host";
    private static final String PORT_KEY = "port";
    private static final String TIMEOUT_KEY = "connectionTimeout";
    private static final String COMPRESSION_ENABLED_KEY = "compressionEnabled";
    private static final String SEND_PRESENCE_KEY = "sendPresence";
    private static final String SAVE_CREDENTIALS_KEY = "saveCredentials";
    private static final String DEFAULT_SETTINGS_KEY = "defaultSettings";
    private static final String RECONNECTION_DELAY_KEY = "reconnectionDelay";
    private static final String RECONNECTION_POLICY_KEY = "reconnectionPolicy";
    private static final String PASS_KEYSTORE = "password";

    private static final String DEFAULT_KEYSTORE = "flicker_keystore";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String userLogin;
    private Context context;
    //private ISecuredDataProvider provider;

    //private static ISettings instance = null;

    public Settings(final Context context) {
        this(context, null);
    }

    public Settings(final Context context, final String userLogin) {

        if(context == null)
            throw  new IllegalArgumentException("context == null");

        this.context = context;
        loadFor(userLogin);
    }

    public String getUser() {
        return userLogin;
    }

    /*public static synchronized  void initInstance(final Context context) {
        initInstance(context, null);
    }*/

    //public static synchronized void initInstance(final Context context, final String userLogin) {
    //    if(instance == null) instance = new Settings(context, userLogin);
    //}

    /*public static ISettings getInstance() {
        return instance;
    }*/

    @Override
    public void loadDefault() {
        loadFor(null);
    }

    @Override
    public void loadFor(String login) {
        this.userLogin = login;
        this.preferences = context.getSharedPreferences(Settings.buildLocation(login), Context.MODE_PRIVATE);
        this.editor = preferences.edit();
    }

    @Override
    public String getServiceName() {
        return preferences.getString(SERVICE_NAME_KEY, "maxlaptop");
    }

    @Override
    public void setPort(int port) {
        editor.putInt(PORT_KEY, port);
        editor.commit();
    }

    @Override
    public int getPort() {
        return preferences.getInt(PORT_KEY, 5222);
    }

    @Override
    public void setConnectionTimeout(int timeout) {
        editor.putInt(TIMEOUT_KEY, timeout);
        editor.commit();
    }

    @Override
    public int getConnectionTimeout() {
        return preferences.getInt(TIMEOUT_KEY, 3000);
    }

    @Override
    public String getHost() {
        return preferences.getString(HOST_KEY, "192.168.0.100");
    }

    @Override
    public void setHost(String host) {
        editor.putString(HOST_KEY, host);
        editor.commit();
    }

    @Override
    public void setServiceName(String serviceName) {
        editor.putString(SERVICE_NAME_KEY, serviceName == null ?
                DEFAULT_SERVICE_NAME : serviceName);
        editor.commit();
    }

    @Override
    public boolean isCompressionEnabled() {
        return preferences.getBoolean(COMPRESSION_ENABLED_KEY, true);
    }

    @Override
    public void setCompressionEnabled(boolean enabled) {
        editor.putBoolean(COMPRESSION_ENABLED_KEY, enabled);
        editor.commit();
    }

    @Override
    public boolean isSendPresence() {
        return preferences.getBoolean(SEND_PRESENCE_KEY, true);
    }

    @Override
    public void setSendPresence(boolean sendPresence) {
        editor.putBoolean(SEND_PRESENCE_KEY, sendPresence);
        editor.commit();
    }

    @Override
    public void setReconnectionInterval(int interval) {
        editor.putInt(RECONNECTION_DELAY_KEY, interval < 1 ? 12 : interval);
        editor.commit();
    }

    @Override
    public int getReconnectionInterval() {
        return preferences.getInt(RECONNECTION_DELAY_KEY, 12);
    }

    @Override
    public void setReconnectionPolicy(ReconnectionManager.ReconnectionPolicy policy) {
        editor.putString(RECONNECTION_POLICY_KEY, policy.toString());
        editor.commit();
    }

    @Override
    public ReconnectionManager.ReconnectionPolicy getReconnetionPolicy() {

        String savedPolicy = preferences.getString(RECONNECTION_POLICY_KEY, null);
        ReconnectionManager.ReconnectionPolicy policy =
                savedPolicy == null ?
                ReconnectionManager.ReconnectionPolicy.FIXED_DELAY :
                        ReconnectionManager.ReconnectionPolicy.valueOf(savedPolicy);

        return policy;
    }

    @Override
    public void setSaveAuthData(boolean save) {
        editor.putBoolean(SAVE_CREDENTIALS_KEY, save);
        editor.commit();
    }

    @Override
    public boolean isSaveAuthData() {
        return preferences.getBoolean(SAVE_CREDENTIALS_KEY, true);
    }

    @Override
    public boolean setCredentials(String login, String password) {
        //FIXME should be replaced with secured data provider!

        SharedPreferences preferences = context.getSharedPreferences(DEFAULT_SETTINGS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("login", login);
        editor.putString("password", password);

        return editor.commit();
    }

    @Override
    public String getSavedPassword() {
        //FIXME should be replaced with secured data provider!
        SharedPreferences preferences = context.getSharedPreferences(DEFAULT_SETTINGS_KEY, Context.MODE_PRIVATE);
        return preferences.getString("password", null);
    }

    @Override
    public String getSavedLogin() {
        //FIXME should be replaced with secured data provider!
        SharedPreferences preferences = context.getSharedPreferences(DEFAULT_SETTINGS_KEY, Context.MODE_PRIVATE);
        return preferences.getString("login", null);
    }

    private static String buildLocation(final String login) {
       return LOCATION.concat(String.valueOf('.')).concat(
                login == null ? DEFAULT_SETTINGS_KEY : login);
    }
}
