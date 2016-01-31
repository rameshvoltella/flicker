package com.ua.max.oliynick.flicker.interfaces;

import android.view.View;

/**
 *
 /**
 * Login activity,
 * it's the first screen that should be
 * shown to the user.
 * This class should be driven by ILoginModel class
 * Created by Максим on 26.01.2016.
 */
public interface ILoginController {

    /**
     * Process login action
     * */
    public void onLogin(View v);

}
