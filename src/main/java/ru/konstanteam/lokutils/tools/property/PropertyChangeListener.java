package ru.konstanteam.lokutils.tools.property;

public interface PropertyChangeListener<T> {
    void changed(T oldValue, T newValue);
}
