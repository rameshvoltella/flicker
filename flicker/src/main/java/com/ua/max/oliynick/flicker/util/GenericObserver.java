package com.ua.max.oliynick.flicker.util;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Максим on 01.02.2016.
 */
public abstract class GenericObserver<T> {

    private T oldValue;
    private T newValue;

    private Observer observer;

    public GenericObserver() {
        this(null);
    }

    public GenericObserver(final T initialValue) {
        init(initialValue);
    }

    public Observer getObserver() {
        return observer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenericObserver<?> that = (GenericObserver<?>) o;

        if (oldValue != null ? !oldValue.equals(that.oldValue) : that.oldValue != null)
            return false;
        if (newValue != null ? !newValue.equals(that.newValue) : that.newValue != null)
            return false;

        return observer.equals(that.observer);

    }

    @Override
    public int hashCode() {
        int result = oldValue != null ? oldValue.hashCode() : 0;
        result = 31 * result + (newValue != null ? newValue.hashCode() : 0);
        result = 31 * result + (observer != null ? observer.hashCode() : 0);
        return result;
    }

    /**
     * This method will be invoked when
     * value of given observable changes
     * */
    public abstract void onValueChanged(Observable observable, T oldValue, T newValue);

    private void init(T initial) {
        observer = new Observer() {

            @Override
            public void update(Observable observable, Object data) {
                T tmp = newValue;
                newValue = (T) data;
                oldValue = tmp;
                onValueChanged(observable, oldValue, newValue);
            }
        };

        newValue = oldValue = initial;
    }

}
