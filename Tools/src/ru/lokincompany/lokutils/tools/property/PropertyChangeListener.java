package ru.lokincompany.lokutils.tools.property;

public interface PropertyChangeListener<T> {
    void changed(T oldValue, T newValue);
}
