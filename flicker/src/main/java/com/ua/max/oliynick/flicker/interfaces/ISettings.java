package com.ua.max.oliynick.flicker.interfaces;

/**
 * Created by Максим on 21.01.2016.
 */
public interface ISettings {

    public void loadDefault();

    public void loadFor(final String login);

    public  String getServiceName();

    public void setPort(int port);

    public int getPort();

    public void setConnectionTimeout(final int timeout);

    public int getConnectionTimeout();

    public String getHost();

    public void setHost(final String host);

    public void setServiceName(final String serviceName);

    public boolean isCompressionEnabled();

    public void setCompressionEnabled(final boolean enabled);

    public boolean isSendPresence();

    public void setSendPresence(final boolean sendPresence);

    public void setSaveAuthData(final boolean save);

    public boolean isSaveAuthData();

    public boolean setCredentials(final String login, final String password);

    public String getSavedPassword();

    public String getSavedLogin();
}
