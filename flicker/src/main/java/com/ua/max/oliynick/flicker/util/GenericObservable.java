package com.ua.max.oliynick.flicker.util;

import java.util.Observable;

/**
 * Created by Максим on 01.02.2016.
 */
public class GenericObservable <T> {

    private class CustomObs extends Observable {

        public void notify(final T value) {
            setChanged();
            notifyObservers(t);
        }
    }

    protected final CustomObs observable;

    protected T t;

    public GenericObservable(T t) {
        this.observable = new CustomObs();

        if(t != null) {
            setValue(t);
        }
    }

    public GenericObservable() {
        this(null);
    }

    public void setValue(T value) {
        t = value;
        observable.notify(t);
    }

    public void addObserver(final GenericObserver<T> observer) {
        observable.addObserver(observer.getObserver());
    }

    public void deleteObserver(final GenericObserver<T> observer) {
        observable.deleteObserver(observer.getObserver());
    }

    public void deleteObservers() {
        observable.deleteObservers();
    }

    public int getObserversCount() {
        return observable.countObservers();
    }

    public T getValue() {
        return t;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenericObservable<?> that = (GenericObservable<?>) o;

        if (!observable.equals(that.observable)) return false;
        return (t != null ? t.equals(that.t) : that.t == null);
    }

    @Override
    public int hashCode() {
        int result = observable.hashCode();
        result = 31 * result + (t != null ? t.hashCode() : 0);
        return result;
    }
}
