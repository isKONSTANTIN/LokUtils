package su.knst.lokutils.tools.property;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractValue<T> implements Value<T> {
    protected T value;

    protected final List<ChangeListener<? super T>> changeListeners = new ArrayList<>();
    protected final List<InvalidationListener<? super T>> invalidationListeners = new ArrayList<>();

    @Override
    public T get() {
        return value;
    }

    @Override
    public void addChangeListener(ChangeListener<T> listener) {
        if (!changeListeners.contains(listener))
            changeListeners.add(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener<T> listener) {
        changeListeners.remove(listener);
    }

    @Override
    public void addInvalidationListener(InvalidationListener<T> listener) {
        if (!invalidationListeners.contains(listener))
            invalidationListeners.add(listener);
    }

    @Override
    public void removeInvalidationListener(InvalidationListener<T> listener) {
        invalidationListeners.remove(listener);
    }

    protected void fireChangeListeners(T oldValue, T newValue) {
        for (ChangeListener<? super T> listener : changeListeners) {
            listener.changed((Value) this, oldValue, newValue);
        }
    }

    protected void fireInvalidationListeners() {
        for (InvalidationListener<? super T> listener : invalidationListeners) {
            listener.invalidated((Value) this);
        }
    }
}
