package com.oceanview.observer;

public interface Observer<T> {
    void update(T event);
}