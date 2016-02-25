package com.ua.max.oliynick.flicker.interfaces;

import com.ua.max.oliynick.flicker.util.ContactItemModel;

import java.util.Collection;

/**
 * Created by Максим on 18.02.2016.
 */
public abstract class IContactsModel implements IInitializible {

    private IContactItemController controller = null;

    public IContactsModel(){}

    public IContactsModel(final IContactItemController controller){
        this.controller = controller;
    }

    public IContactItemController getController() {
        return controller;
    }

    public void setController(IContactItemController controller) {
        this.controller = controller;
    }

    public abstract Collection<ContactItemModel> getContacts();

    public abstract Collection<ContactItemModel> getContacts(final String query);

    protected abstract void onContactListUpdate(ContactItemModel item);

}
