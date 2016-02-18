package com.ua.max.oliynick.flicker.util;

/**
 * Created by Максим on 18.02.2016.
 */
public class StringUtils {

    /**
     * Returns jid address without resource specifier
     * */
    public static String parseBareJid(final String jid) {
        if(jid == null) return null;

        int indx = jid.indexOf('/');

        return jid.substring(0, indx < 0 ? jid.length() : indx);
    }

    /**
     * Returns user name from given jid
     * */
    public static String parseBareName(final String jid) {
        if(jid == null) return null;

        int indx = jid.indexOf('@');

        return jid.substring(0, indx < 0 ? jid.length() : indx);
    }

}
