package com.ua.max.oliynick.flicker.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ua.max.oliynick.flicker.interfaces.ISettings;

/**
 * Created by Максим on 21.01.2016.
 */
public class Settings implements ISettings {

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

    private SharedPreferences preferences;
    private String userLogin;
    private Context context;

    private static ISettings instance = null;

    private Settings(final Context context) {
        this(context, null);
    }

    private Settings(final Context context, final String userLogin) {

        if(context == null)
            throw  new IllegalArgumentException("context == null");

        this.context = context;
        loadFor(userLogin);
    }

    public String getUser() {
        return userLogin;
    }

    public static synchronized  void initInstance(final Context context) {
        initInstance(context, null);
    }

    public static synchronized void initInstance(final Context context, final String userLogin) {
        if(instance == null) instance = new Settings(context, userLogin);
    }

    public static ISettings getInstance() {
        return instance;
    }

    @Override
    public void loadDefault() {
        loadFor(null);
    }

    @Override
    public void loadFor(String login) {
        this.userLogin = login;
        this.preferences = context.getSharedPreferences(Settings.buildLocation(login), Context.MODE_PRIVATE);
    }

    @Override
    public String getServiceName() {
        return preferences.getString(SERVICE_NAME_KEY, "maxlaptop");
    }

    @Override
    public void setPort(int port) {
        preferences.edit().putInt(PORT_KEY, port);
    }

    @Override
    public int getPort() {
        return preferences.getInt(PORT_KEY, 5222);
    }

    @Override
    public void setConnectionTimeout(int timeout) {
        preferences.edit().putInt(TIMEOUT_KEY, timeout);
    }

    @Override
    public int getConnectionTimeout() {
        return preferences.getInt(TIMEOUT_KEY, 3000);
    }

    @Override
    public String getHost() {
        return preferences.getString(HOST_KEY, "192.168.1.104");
    }

    @Override
    public void setHost(String host) {
        preferences.edit().putString(HOST_KEY, host);
    }

    @Override
    public void setServiceName(String serviceName) {
        preferences.edit().putString(SERVICE_NAME_KEY, serviceName == null ?
                DEFAULT_SERVICE_NAME : serviceName);
    }

    @Override
    public boolean isCompressionEnabled() {
        return preferences.getBoolean(COMPRESSION_ENABLED_KEY, true);
    }

    @Override
    public void setCompressionEnabled(boolean enabled) {
        preferences.edit().putBoolean(COMPRESSION_ENABLED_KEY, enabled);
    }

    @Override
    public boolean isSendPresence() {
        return preferences.getBoolean(SEND_PRESENCE_KEY, true);
    }

    @Override
    public void setSendPresence(boolean sendPresence) {
        preferences.edit().putBoolean(SEND_PRESENCE_KEY, sendPresence);
    }

    @Override
    public void setSaveAuthData(boolean save) {
        preferences.edit().putBoolean(SAVE_CREDENTIALS_KEY, save);
    }

    @Override
    public boolean isSaveAuthData() {
        return preferences.getBoolean(SAVE_CREDENTIALS_KEY, true);
    }

    private static String buildLocation(final String login) {
       return LOCATION.concat(String.valueOf('.')).concat(
                login == null ? DEFAULT_SETTINGS_KEY : login);
    }
}
