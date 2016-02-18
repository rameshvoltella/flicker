package com.ua.max.oliynick.flicker.roboguice;

import android.app.Application;
import android.content.Context;

import com.google.inject.AbstractModule;
import com.ua.max.oliynick.flicker.interfaces.IContactsModel;
import com.ua.max.oliynick.flicker.interfaces.ILoginModel;
import com.ua.max.oliynick.flicker.model.ContactModel;
import com.ua.max.oliynick.flicker.model.LoginModel;

/**
 * Created by Максим on 27.01.2016.
 */
public class Bindings extends AbstractModule {

    private Application application;

    public Bindings(final Application application) {
        this.application = application;
    }

    @Override
    protected void configure() {
        //bind(ILoginModel.class).to(LoginModel.class);
        final Context context = application.getApplicationContext();

        bind(ILoginModel.class).toInstance(new LoginModel(context));
        bind(IContactsModel.class).toInstance(new ContactModel());
    }
}
