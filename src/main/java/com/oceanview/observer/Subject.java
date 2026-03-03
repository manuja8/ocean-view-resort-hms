package com.oceanview.observer;

import java.util.ArrayList;
import java.util.List;

public class Subject<T> {

    private final List<Observer<T>> observers = new ArrayList<>();

    public void register(Observer<T> o) {
        observers.add(o);
    }

    public void notifyObservers(T event) {
        for (Observer<T> o : observers) o.update(event);
    }
}