package com.ua.max.oliynick.flicker.interfaces;

/**
 * Created by Максим on 31.01.2016.
 */
public interface ISecuredDataProvider {

    public boolean add(final String key, final String value);

    public boolean delete(final String key);

    public String get(final String key);
}
