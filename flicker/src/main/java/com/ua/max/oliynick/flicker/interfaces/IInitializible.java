package com.ua.max.oliynick.flicker.interfaces;

import android.content.Context;

/**
 * This interface should be implemented
 * by classes which requires initialization
 * (ex. long-running initialization process)
 * Created by Максим on 26.01.2016.
 */
public interface IInitializible {

    /**
     * Runs an initialization process using given
     * context
     * @param context provides (if needed) with global
     * information and application specific
     * resources
     * */
    public void initialize(Context context);

}
